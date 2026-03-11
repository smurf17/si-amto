<script setup lang="ts">
import { ref } from 'vue'
import BreadcrumbDefault from '../components/Breadcrumbs/BreadcrumbDefault.vue'
import Table from '../components/Student/TableStudents.vue'
import DefaultLayout from '../layouts/DefaultLayout.vue'

const pageTitle = ref('Data Mahasiswa')
</script>

<template>
  <DefaultLayout>
    <!-- Breadcrumb Start -->
    <BreadcrumbDefault :pageTitle="pageTitle" />
    <!-- Breadcrumb End -->

    <div class="flex flex-col gap-10">
      <Table />
    </div>
  </DefaultLayout>
</template>
