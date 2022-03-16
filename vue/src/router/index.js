import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../layout/Layout.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: "/home",
        children: [{
            path: 'home',
            name: 'Home',
            component: () => import("@/views/HomeView"),
        }]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import("@/components/Login")
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import("@/components/Register")
    },

]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
