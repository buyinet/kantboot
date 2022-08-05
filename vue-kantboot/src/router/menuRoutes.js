import request from '@/api/request.js'
// localStorage.setItem("menus","[]");
if (localStorage.getItem("menus") == null || localStorage.getItem("menus") == "") {
	localStorage.setItem("menus", "[]");
}
var menus = JSON.parse(localStorage.getItem("menus"));
console.log(menus);
const routes = [];

for (let i = 0; i < menus.length; i++) {
	let item = menus[i];
	let itemRoute = {
		path: item.path,
		name: item.name,
		component: () => import(`../views/${item.pagePath}`),
		children: []
	};

	for (let j = 0; j < item.children.length; j++) {
		let item1 = item.children[j];
		itemRoute.children.push({
			path: item1.path,
			name: item1.name,
			component: () => import(`../views/${item1.pagePath}`)
		});
	}
	routes.push(itemRoute);
}

export default routes
