<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import DefaultCard from '../Forms/DefaultCard.vue'
import InputGroup from '../Forms/InputGroup.vue'

const pageTitle = ref('Tambah Mahasiswa')
</script>

<template>
  <div class="flex flex-col gap-9">
    <DefaultCard cardTitle="Tambah Mahasiswa">
      <form action="#">
        <div class="p-6.5">
          <div class="mb-4.5 flex flex-col gap-6 xl:flex-row">
            <InputGroup
              label="Nama Mahasiswa"
              type="text"
              customClasses="w-full xl:w-1/2"
            />

            <InputGroup
              label="NIM"
              type="text"
              customClasses="w-full xl:w-1/2"
            />
          </div>

          <div class="mb-4.5 flex flex-col gap-6 xl:flex-row">
            <InputGroup
              label="Tempat Lahir"
              type="text"
              customClasses="w-full xl:w-1/2"
            />

            <InputGroup
              label="Tanggal Lahir"
              type="text"
              customClasses="w-full xl:w-1/2"
            />
          </div>

          <div class="mb-4.5 flex flex-col gap-6 xl:flex-row">
            <InputGroup
              label="Program Studi"
              type="text"
              customClasses="w-full xl:w-1/2"
            />

            <InputGroup
              label="Kurikulum"
              type="text"
              customClasses="w-full xl:w-1/2"
            />
          </div>

          <div class="mb-7.5 flex flex-wrap gap-2 xl:gap-5">
            <button
            type="button"
            class="flex justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-meta-5"
            >
            <router-link
                :to="`/students`"
                class="fill-current"
                >
                Tambah
                </router-link>
            </button>
            <button
              type="button"
              class="flex justify-center rounded border border-stroke bg-transparent p-3 font-medium text-black hover:bg-gray-2 dark:border-strokedark dark:text-white dark:hover:bg-meta-4"
              @click="$router.back()"
            >
              Kembali
            </button>
          </div>
        </div>
      </form>
    </DefaultCard>
  </div>
</template>