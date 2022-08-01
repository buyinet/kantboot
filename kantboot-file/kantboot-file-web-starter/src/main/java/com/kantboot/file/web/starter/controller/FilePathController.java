package com.kantboot.file.web.starter.controller;

import com.kantboot.file.module.entity.KfmFilePath;
import com.kantboot.util.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file_path")
public class FilePathController extends BaseController<KfmFilePath, Long> {

}
