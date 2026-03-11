<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import DefaultCard from '../Forms/DefaultCard.vue'
import InputGroup from '../Forms/InputGroup.vue'

interface DetailStudent {
  id: number
  studentNumber: string
  name: string
  birthplace: string
  birthDate: string
  majorName: string
  curriculumName: string
}

const pageTitle = ref('Detail Mahasiswa')
const studentDetail = ref<DetailStudent | null>(null)
const fetchStudentDetail = async () => {
  try {
    const response = await axios.get('/api/students/1')
    // Perhatikan response.data.data[0]
    if (response.data.success && response.data.data.length > 0) {
      studentDetail.value = response.data.data[0]
    }
  } catch (error) {
    console.error('Error fetching student detail:', error)
  }
}

onMounted(() => {
  fetchStudentDetail()
})
</script>

<template>
  <div class="flex flex-col gap-9">
    <DefaultCard cardTitle="Detail Mahasiswa">
      <form action="#" v-if="studentDetail">
        <div class="p-6.5">
          <div class="mb-4.5 flex flex-col gap-6 xl:flex-row">
            <InputGroup
              label="Nama Mahasiswa"
              type="text"
              customClasses="w-full xl:w-1/2"
              v-model="studentDetail.name"
            />

            <InputGroup
              label="NIM"
              type="text"
              customClasses="w-full xl:w-1/2"
              :modelValue="studentDetail.studentNumber"
            />
          </div>

          <div class="mb-4.5 flex flex-col gap-6 xl:flex-row">
            <InputGroup
              label="Tempat Lahir"
              type="text"
              customClasses="w-full xl:w-1/2"
              :modelValue="studentDetail.birthplace"
            />

            <InputGroup
              label="Tanggal Lahir"
              type="text"
              customClasses="w-full xl:w-1/2"
              :modelValue="studentDetail.birthDate"
            />
          </div>

          <div class="mb-4.5 flex flex-col gap-6 xl:flex-row">
            <InputGroup
              label="Program Studi"
              type="text"
              customClasses="w-full xl:w-1/2"
              :modelValue="studentDetail.majorName"
            />

            <InputGroup
              label="Kurikulum"
              type="text"
              customClasses="w-full xl:w-1/2"
              :modelValue="studentDetail.curriculumName"
            />
          </div>

          <button
            type="button"
            class="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90"
            @click="$router.back()"
          >
            Kembali
          </button>
        </div>
      </form>
      <div v-else class="p-6.5 text-center">Loading data...</div>
    </DefaultCard>
  </div>
</template>