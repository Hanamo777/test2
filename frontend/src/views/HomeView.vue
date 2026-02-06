<template>
  <div class="home">
    <h1>  일부러 취약하게 만들어보자, God Bless you. test1</h1>
    <button @click="$router.push('/test')" class="test-button">테스트 페이지로 이동</button>
  </div>
</template>

<script>
export default {
  name: 'HomeView'
}
</script>

<style scoped>
.home {
  text-align: center;
  padding: 2rem;
}

.test-button {
  padding: 1rem 2rem;
  font-size: 1.2rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.test-button:hover {
  background-color: #45a049;
}
</style> 