import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../layout/Layout.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: "/user",
        children: [{
            path: 'user',
            name: 'User',
            component: () => import("@/views/User"),
        },
        {
            path: '/book',
            name: 'Book',
            component: () => import("@/views/Book")
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
