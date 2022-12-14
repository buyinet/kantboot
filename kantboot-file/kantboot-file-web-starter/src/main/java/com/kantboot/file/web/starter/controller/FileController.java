package com.kantboot.file.web.starter.controller;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.Protocol;
import com.kantboot.file.module.entity.KfmFile;
import com.kantboot.file.module.entity.KfmFileOss;
import com.kantboot.file.module.entity.KfmFileParent;
import com.kantboot.file.module.repository.CesFileParentRepository;
import com.kantboot.file.module.repository.KmfFileRepository;
import com.kantboot.system.user.module.service.ISysSettingService;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.common.util.RedisUtil;
import com.kantboot.util.common.util.RestResult;
import com.kantboot.util.core.controller.BaseController;
import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/file")
public class FileController extends BaseController<KfmFile, Long> {





    @Resource
    CesFileParentRepository cesFileParentRepository;

    @Resource
    KmfFileRepository cesFileRepository;

    @Resource
    ISysSettingService sysSettingService;

    //获取上传到oss后的名字
    private static String fileName(MultipartFile myfile) {

        Calendar calendar = Calendar.getInstance();
        String name = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.DATE) + "/" + UUID.randomUUID().toString().replace("-", "") +
                myfile.getOriginalFilename();

        return name;
    }


    @RequestMapping("/get_visit_url/{id}")
    public RestResult<?> getVistUrl(@PathVariable("id") Long id) {

        KfmFile sysFileStore = cesFileRepository.findById(id).get();
        if (sysFileStore.getStorageType().equals("path")) {
            return RestResult.success(sysSettingService.getSetting().getFileVisitUrl() + id, "获取成功");
        }

        if (sysFileStore.getStorageType().equals("oss")) {
            Date expiration = new Date(new Date().getTime() + 1000 * 30);
            OSS oss = new OSSClientBuilder()
                    .build(sysFileStore.getFileOss().getEndpoint(),
                            sysFileStore.getFileOss().getAccessKeyId(),
                            sysFileStore.getFileOss().getAccessKeySecret());

            URL url = oss
                    .generatePresignedUrl(sysFileStore.getFileOss().getBucketName(),
                            sysFileStore.getPath(), expiration);
            return RestResult.success(url.toString(), "获取成功");
        }
        return RestResult.success("错误存储模式", "错误存储模式");
    }

    public File m2f(MultipartFile file) throws Exception {
        File f = File.createTempFile(UUID.randomUUID().toString(), "." + FilenameUtils.getExtension(file.getOriginalFilename()));
        file.transferTo(f);
        return f;
    }

    public InputStream bufferedImageToInputStream(BufferedImage image, String formatName) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, formatName, os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {

        }
        return null;
    }

    public void saveBit(InputStream inStream,String fileName) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//创建一个Buffer字符串
        byte[] buffer = new byte[1024];
//每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
//使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
//用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
//关闭输入流
        inStream.close();
//把outStream里的数据写入内存

//得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = outStream.toByteArray();
//new一个文件对象用来保存图片，默认保存当前工程根目录
        File imageFile = new File(fileName);
//创建输出流
        FileOutputStream fileOutStream = new FileOutputStream(imageFile);
//写入数据
        fileOutStream.write(data);

    }

    /**
     * 上传文件
     *
     * @param file
     * @param bodyName
     * @param bodyField
     * @param name
     * @param content
     * @return
     */
    @SneakyThrows
    @PostMapping("/upload/{bodyName}/{bodyField}")
    public RestResult<?> upload(
            @RequestParam("file") MultipartFile file,
            @PathVariable("bodyName") String bodyName,
            @PathVariable("bodyField") String bodyField,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "content", required = false) String content
    ) {
        KfmFileParent byBodyNameAndBodyField
                = cesFileParentRepository.findByBodyNameAndBodyField(bodyName, bodyField);

        String objectName = fileName(file);
        String formatName = null;
        String[] split = objectName.split("\\.");
        if (split.length != 0) {
            formatName = split[split.length - 1];
        }

        InputStream inputStream = null;
        InputStream inputStream1=null;
        if (byBodyNameAndBodyField.getUseWatermark()) {
            inputStream1=file.getInputStream();
            System.out.println("byBodyNameAndBodyField.getFileUrlByWatermark()="+byBodyNameAndBodyField.getFileUrlByWatermark());
            BufferedImage bufferedImage = Thumbnails.of(ImageIO.read(m2f(file)))
                    .scale(1f)
                    .watermark(Positions.CENTER, ImageIO.read(new URL(byBodyNameAndBodyField.getFileUrlByWatermark())), 1f)
                    .asBufferedImage();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            inputStream = bufferedImageToInputStream(bufferedImage, formatName);

            System.out.println(inputStream);

        } else {
            inputStream = file.getInputStream();
        }

        if (byBodyNameAndBodyField.getStorageType().equals("path")) {
            String fileName = byBodyNameAndBodyField.getFilePath().getPath() + "/" + objectName;
            System.out.println("fileName = " + fileName);
            String substring = fileName.substring(0, fileName.lastIndexOf("/"));
            File file1 = new File(substring);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            try {
//                file.transferTo(new File(fileName));
                saveBit(inputStream,fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            KfmFile sysFileStore = new KfmFile();
            sysFileStore.setFilePathId(byBodyNameAndBodyField.getFilePathId());
            sysFileStore.setPath(fileName);

            sysFileStore.setPathByNoWatermark(fileName);
            if(byBodyNameAndBodyField.getUseWatermark()){
                String fileNamew = byBodyNameAndBodyField.getFilePath().getPath() + "/" + "no_watermark/"+objectName;
                System.out.println("fileName = " + fileNamew);
                String substringw = fileNamew.substring(0, fileNamew.lastIndexOf("/"));
                File file1w = new File(substringw);
                if (!file1w.exists()) {
                    file1w.mkdirs();
                }
                try {
//                file.transferTo(new File(fileName));
                    saveBit(inputStream1,fileNamew);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                sysFileStore.setPathByNoWatermark(fileNamew);
            }
            sysFileStore.setName(name);
            sysFileStore.setContent(content);
            sysFileStore.setStorageType(byBodyNameAndBodyField.getStorageType());
            sysFileStore.setFileParentId(byBodyNameAndBodyField.getId());

            KfmFile save = cesFileRepository.save(sysFileStore);
            KfmFile kfmFile = cesFileRepository.findById(save.getId()).get();
            HashMap hashMap = JSON.parseObject(JSON.toJSONString(kfmFile), HashMap.class);
            String visitUrlById = sysSettingService.getSetting().getFileVisitUrl() + kfmFile.getId();
            hashMap.put("visitUrlById",visitUrlById);
            return RestResult.success(hashMap, "上传成功");
        }

        if (byBodyNameAndBodyField.getStorageType().equals("oss")) {
            KfmFileOss fileOss = byBodyNameAndBodyField.getFileOss();
            // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
            String endpoint = fileOss.getEndpoint();

            //LTAI78XQAZq2s5Rv
            //qdyZxR0x4LoUpTVbuyvCGdcrhEyw7H
            // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
            String accessKeyId = fileOss.getAccessKeyId();
            String accessKeySecret = fileOss.getAccessKeySecret();
            // 填写Bucket名称，例如examplebucket。
            String bucketName = fileOss.getBucketName();

            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            String fileName = objectName;
            if (fileOss.getBodyFolder() != null) {
                fileName = fileOss.getBodyFolder() + "/" + objectName;
            }
            try {
//                inputStream = file.getInputStream();
                // 创建PutObject请求。
                ossClient.putObject(bucketName, fileName, inputStream);

            } catch (Exception oe) {

            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
            KfmFile sysFileStore = new KfmFile();
            sysFileStore.setFileOssId(byBodyNameAndBodyField.getFileOssId());
            sysFileStore.setPath(fileName);
            sysFileStore.setPathByNoWatermark(fileName);
            if(byBodyNameAndBodyField.getUseWatermark()){
                String fileNamew =  fileOss.getBodyFolder() + "/" + "no_watermark/" + objectName;;
                System.out.println("fileName = " + fileNamew);
                String substringw = fileNamew.substring(0, fileNamew.lastIndexOf("/"));
                File file1w = new File(substringw);
                if (!file1w.exists()) {
                    file1w.mkdirs();
                }

                // 创建OSSClient实例。
                OSS ossClientw = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

                try {
//                inputStream = file.getInputStream();
                    // 创建PutObject请求。
                    ossClientw.putObject(bucketName, fileNamew, inputStream1);

                } catch (Exception oe) {

                } finally {
                    if (ossClientw != null) {
                        ossClientw.shutdown();
                    }
                }

                sysFileStore.setPathByNoWatermark(fileNamew);
            }
            sysFileStore.setName(name);
            sysFileStore.setContent(content);

            sysFileStore.setStorageType(byBodyNameAndBodyField.getStorageType());
            sysFileStore.setFileParentId(byBodyNameAndBodyField.getId());
            System.out.println("JSON.toJSONString(sysFileStore) = " + JSON.toJSONString(sysFileStore));
            KfmFile save = cesFileRepository.save(sysFileStore);
            KfmFile kfmFile = cesFileRepository.findById(save.getId()).get();
            HashMap hashMap = JSON.parseObject(JSON.toJSONString(kfmFile), HashMap.class);
            String visitUrlById = sysSettingService.getSetting().getFileVisitUrl() + kfmFile.getId();
            hashMap.put("visitUrlById",visitUrlById);
            return RestResult.success(hashMap, "上传成功");
        }

        return RestResult.error("上传错误");

    }

    /**
     * 通过图片名称查看图片
     *
     * @param bodyName
     * @param bodyField
     * @param name
     */
    @SneakyThrows
    @RequestMapping("/visit/{bodyName}/{bodyField}/{name}")
    public void visitBodyNameBodyFileldName(
            @PathVariable("bodyName") String bodyName,
            @PathVariable("bodyField") String bodyField,
            @PathVariable("name") String name,
            HttpServletResponse response
    ) {
        List<KfmFile> byFileParentByBodyNameAndFileParentByBodyFieldAndName = cesFileRepository.findByFileParentBodyNameAndFileParentBodyFieldAndName(bodyName, bodyField, name);
//        response.set
        KfmFile sysFileStore = byFileParentByBodyNameAndFileParentByBodyFieldAndName.get(0);
        if (sysFileStore.getStorageType().equals("path")) {
            String realPath = sysFileStore.getPath();
            String substring = realPath.substring(realPath.lastIndexOf("/"));
            response.setHeader("Content-Disposition", "filename=" + UUID.randomUUID().toString() + "." + substring);
            FileInputStream inputStream = new FileInputStream(new File(realPath));
            ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
            return;
        }

        if (sysFileStore.getStorageType().equals("oss")) {
            Date expiration = new Date(new Date().getTime() + 1000 * 30);
            ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
            clientBuilderConfiguration.setProtocol(Protocol.HTTPS);
            OSS oss = new OSSClientBuilder()
                    .build(sysFileStore.getFileOss().getEndpoint(),
                            sysFileStore.getFileOss().getAccessKeyId(),
                            sysFileStore.getFileOss().getAccessKeySecret(),
                            clientBuilderConfiguration);
            URL url = oss
                    .generatePresignedUrl(sysFileStore.getFileOss().getBucketName(),
                            sysFileStore.getPath(), expiration);
            response.sendRedirect(url.toString());
            return;
        }
    }

    @Resource
    RestTemplate restTemplate;

    @Resource
    HttpServletRequest request;

    @Resource
    RedisUtil redisUtil;


    /**
     * @param id       文件id
     * @param uid      临时访问码
     * @param response
     * @return
     */
    @SneakyThrows
    @RequestMapping("/visit/{id}/{uid}")
    public void pathView2(
            @PathVariable("id") Long id,
            @PathVariable("uid") String uid,
            HttpServletResponse response) {
        String fileId = redisUtil.get("file_visit_url:" + uid + ":file_id");
        if (!(id + "").equals(fileId)) {
            throw new BaseException(3000, "访问失败");
        }
//        response.setCharacterEncoding("UTF-8");
        KfmFile sysFileStore = cesFileRepository.findById(id).get();
        System.out.println(sysFileStore.getFileParent().getAuthorizeVisit());
        if (sysFileStore.getStorageType().equals("path")) {
            String realPath = sysFileStore.getPath();
            String substring = realPath.substring(realPath.lastIndexOf("/"));
            response.setHeader("Content-Disposition", "filename=" + UUID.randomUUID().toString() + "." + substring);
            FileInputStream inputStream = new FileInputStream(new File(realPath));
            ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
            return;
        }

        if (sysFileStore.getStorageType().equals("oss")) {
            Date expiration = new Date(new Date().getTime() + 1000 * 30);

            OSS oss = new OSSClientBuilder()
                    .build(sysFileStore.getFileOss().getEndpoint(),
                            sysFileStore.getFileOss().getAccessKeyId(),
                            sysFileStore.getFileOss().getAccessKeySecret());
//            oss.setBucketTransferAcceleration(sysFileStore.getFileOss().getBucketName(), true);
            URL url = oss
                    .generatePresignedUrl(sysFileStore.getFileOss().getBucketName(),
                            sysFileStore.getPath(), expiration);
            response.sendRedirect(url.toString());
            return;
        }
    }

    /**
     * 查看在本地存储的文件
     */
    @SneakyThrows
    @RequestMapping("/visit/{id}")
    public void pathView(@PathVariable("id") Long id, HttpServletResponse response) {

        System.out.println("======");
//        response.setCharacterEncoding("UTF-8");
        KfmFile sysFileStore = cesFileRepository.findById(id).get();
        System.out.println(sysFileStore.getFileParent().getAuthorizeVisit());
        if (sysFileStore.getFileParent().getAuthorizeVisit()) {
            System.out.println("=====111111");
            HttpHeaders headers = new HttpHeaders();
            headers.add("token", request.getHeader("token"));
            headers.add("User-Agent", request.getHeader("User-Agent"));
            HashMap<String, Long> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("fileId", id);

            HttpEntity<HashMap<String, Long>> requestEntity = new HttpEntity(stringStringHashMap, headers);
            RestResult<Boolean> restResult = restTemplate.postForObject(sysFileStore.getFileParent().getAuthorizeVisitCallbackUrl(),
                    requestEntity, RestResult.class);
            if (!restResult.getData()) {
                throw new BaseException(3000, "没有该文件的访问授权权限");
            }
            if (restResult.getData()) {

                response.setContentType("json/application");
                HashMap<String, Object> map = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                redisUtil.setEx("file_visit_url:" + uuid + ":file_id", id + "", 30l, TimeUnit.MINUTES);
                String url = sysSettingService.getSetting().getFileVisitUrl() + id + "/" + uuid;

                map.put("url", url);

                RestResult<?> result = RestResult.success(map, "获取成功");
                PrintWriter writer = response.getWriter();
                writer.println(JSON.toJSONString(result));
                writer.close();
            }
//            System.out.println("JSON.toJSONString(restResult) = " + JSON.toJSONString(restResult));
        }

        if (sysFileStore.getStorageType().equals("path")) {
            String realPath = sysFileStore.getPath();
            String substring = realPath.substring(realPath.lastIndexOf("/"));
            response.setHeader("Content-Disposition", "filename=" + UUID.randomUUID().toString() + "." + substring);
            FileInputStream inputStream = new FileInputStream(new File(realPath));
            ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
            return;
        }

        if (sysFileStore.getStorageType().equals("oss")) {
            Date expiration = new Date(new Date().getTime() + 1000 * 30);

            OSS oss = new OSSClientBuilder()
                    .build(sysFileStore.getFileOss().getEndpoint(),
                            sysFileStore.getFileOss().getAccessKeyId(),
                            sysFileStore.getFileOss().getAccessKeySecret());
//            oss.setBucketTransferAcceleration(sysFileStore.getFileOss().getBucketName(), true);
            URL url = oss
                    .generatePresignedUrl(sysFileStore.getFileOss().getBucketName(),
                            sysFileStore.getPath(), expiration);
            response.sendRedirect(url.toString());
            return;
        }
    }

}
