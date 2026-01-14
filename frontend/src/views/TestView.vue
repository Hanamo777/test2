<template>
  <div class="test">
    <h1>게시판 CRUD 테스트</h1>

    <div class="card">
      <h2>{{ editingId ? '글 수정' : '글 작성' }}</h2>
      <div class="field">
        <label>제목</label>
        <input v-model.trim="form.title" placeholder="제목" />
      </div>
      <div class="field">
        <label>내용</label>
        <textarea v-model.trim="form.content" placeholder="내용" rows="5"></textarea>
      </div>
      <div class="row">
        <button class="primary" @click="submit" :disabled="loading">
          {{ editingId ? '수정 저장' : '등록' }}
        </button>
        <button v-if="editingId" class="ghost" @click="cancelEdit" :disabled="loading">수정 취소</button>
        <button class="ghost" @click="reload" :disabled="loading">새로고침</button>
        <span class="hint" v-if="loading">처리중...</span>
      </div>
      <p class="error" v-if="error">{{ error }}</p>
    </div>

    <div class="card">
      <h2>글 목록</h2>
      <div v-if="posts.length === 0" class="empty">아직 글이 없습니다.</div>
      <div v-for="p in posts" :key="p.id" class="post">
        <div class="post-main">
          <div class="title">#{{ p.id }} {{ p.title }}</div>
          <div class="content">{{ p.content }}</div>
          <div class="meta" v-if="p.createdAt">createdAt: {{ formatDate(p.createdAt) }}</div>
        </div>
        <div class="actions">
          <button class="ghost" @click="startEdit(p)" :disabled="loading">수정</button>
          <button class="danger" @click="remove(p.id)" :disabled="loading">삭제</button>
        </div>
      </div>
    </div>

    <button @click="$router.push('/')" class="home-button">홈으로 돌아가기</button>
  </div>
</template>

<script>
export default {
  name: 'TestView',
  data() {
    return {
      posts: [],
      form: { title: '', content: '' },
      editingId: null,
      loading: false,
      error: ''
    }
  },
  mounted() {
    this.reload()
  },
  methods: {
    api(path) {
      return `/api${path}`
    },
    formatDate(iso) {
      try {
        return new Date(iso).toLocaleString()
      } catch {
        return iso
      }
    },
    async reload() {
      this.error = ''
      this.loading = true
      try {
        const res = await fetch(this.api('/posts'))
        if (!res.ok) throw new Error(`목록 조회 실패 (${res.status})`)
        this.posts = await res.json()
      } catch (e) {
        this.error = e?.message || String(e)
      } finally {
        this.loading = false
      }
    },
    startEdit(p) {
      this.editingId = p.id
      this.form.title = p.title
      this.form.content = p.content
      this.error = ''
    },
    cancelEdit() {
      this.editingId = null
      this.form.title = ''
      this.form.content = ''
      this.error = ''
    },
    async submit() {
      this.error = ''
      if (!this.form.title || !this.form.content) {
        this.error = '제목/내용을 입력해주세요.'
        return
      }

      this.loading = true
      try {
        if (this.editingId) {
          const res = await fetch(this.api(`/posts/${this.editingId}`), {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title: this.form.title, content: this.form.content })
          })
          if (!res.ok) throw new Error(`수정 실패 (${res.status})`)
        } else {
          const res = await fetch(this.api('/posts'), {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title: this.form.title, content: this.form.content })
          })
          if (!res.ok) throw new Error(`등록 실패 (${res.status})`)
        }

        this.cancelEdit()
        await this.reload()
      } catch (e) {
        this.error = e?.message || String(e)
      } finally {
        this.loading = false
      }
    },
    async remove(id) {
      this.error = ''
      this.loading = true
      try {
        const res = await fetch(this.api(`/posts/${id}`), { method: 'DELETE' })
        if (!res.ok && res.status !== 204) throw new Error(`삭제 실패 (${res.status})`)
        await this.reload()
      } catch (e) {
        this.error = e?.message || String(e)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.test {
  text-align: center;
  padding: 2rem;
}

.card {
  max-width: 860px;
  margin: 0 auto 1.25rem auto;
  padding: 1rem 1.25rem;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  background: #fff;
  text-align: left;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
  margin: 0.75rem 0;
}

input, textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 1rem;
}

.row {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.hint {
  color: #6b7280;
  font-size: 0.95rem;
}

.error {
  margin-top: 0.75rem;
  color: #b91c1c;
  font-weight: 600;
}

.empty {
  color: #6b7280;
  padding: 0.75rem 0;
}

.post {
  display: flex;
  gap: 0.75rem;
  justify-content: space-between;
  padding: 0.85rem 0;
  border-top: 1px solid #f3f4f6;
}

.post-main .title {
  font-weight: 800;
  margin-bottom: 0.35rem;
}

.post-main .content {
  white-space: pre-wrap;
  color: #111827;
}

.meta {
  margin-top: 0.35rem;
  color: #6b7280;
  font-size: 0.9rem;
}

.actions {
  display: flex;
  gap: 0.5rem;
  align-items: flex-start;
}

.primary {
  padding: 0.75rem 1rem;
  background-color: #16a34a;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.ghost {
  padding: 0.75rem 1rem;
  background-color: transparent;
  color: #111827;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  cursor: pointer;
}

.danger {
  padding: 0.75rem 1rem;
  background-color: #dc2626;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.home-button {
  padding: 1rem 2rem;
  font-size: 1.2rem;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.home-button:hover {
  background-color: #1976D2;
}
</style> 