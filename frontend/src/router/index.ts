import { createRouter, createWebHistory } from 'vue-router'
import StudentsView from "../views/StudentsView.vue"
import StudentsDetailView from "../views/StudentsDetailView.vue"
import StudentsCreate from "../views/StudentsCreate.vue"

const routes = [
    {
      path: '/students',
      name: 'students',
      component: StudentsView,
      meta: {
        title: 'Students'
      }
    },
    {
      path: '/students/:id',
      name: 'student-detail',
      component: StudentsDetailView,
      meta: {
        title: 'Student Detail'
      }
    },
    {
      path: '/students/create',
      name: 'student-create',
      component: StudentsCreate,
      meta: {
        title: 'Create Student'
      }
    }
]


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    return savedPosition || { left: 0, top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  document.title = `SI-AMTO ${to.meta.title} | Admin`
  next()
})

export default router