import {
	createRouter,
	createWebHistory
} from 'vue-router'
import menuRoutes from './menuRoutes.js'


const routes = [{
		path: '/login',
		name: 'Login',
		component: () => import('@/views/Login.vue')
	},
	{
		path: '/',
		name: 'Main',
		component: () => import('@/views/Main.vue'),
		children: menuRoutes
	}
]

const router = createRouter({
	history: createWebHistory(process.env.BASE_URL),
	routes
})

export default router
