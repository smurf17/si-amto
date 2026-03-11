<script setup lang="ts">
import BreadcrumbDefault from '../components/Breadcrumbs/BreadcrumbDefault.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';
import CreateStudents from '../components/Student/CreateStudents.vue';
import { ref } from 'vue';

const pageTitle = ref('Tambah Mahasiswa')
</script>

<template>
  <DefaultLayout>
    <!-- Breadcrumb Start -->
    <BreadcrumbDefault :pageTitle="pageTitle" />
    <!-- Breadcrumb End -->

    <div class="grid grid-cols-1 gap-9">
        <CreateStudents />
    </div>
  </DefaultLayout>
</template>