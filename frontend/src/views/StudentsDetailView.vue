<script setup lang="ts">
import BreadcrumbDefault from '../components/Breadcrumbs/BreadcrumbDefault.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';
import DetailStudents from '../components/Student/DetailStudents.vue';
import { ref } from 'vue';

const pageTitle = ref('Detail Mahasiswa')
</script>

<template>
  <DefaultLayout>
    <!-- Breadcrumb Start -->
    <BreadcrumbDefault :pageTitle="pageTitle" />
    <!-- Breadcrumb End -->

    <div class="grid grid-cols-1 gap-9">
        <DetailStudents />
    </div>
  </DefaultLayout>
</template>