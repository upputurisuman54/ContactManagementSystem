import { useState } from "react";
import api from "../api";
import "../styles/modal.css";

export default function TagManager({ tags, onClose, onChanged }) {
  const [newTag, setNewTag] = useState("");
  const [error, setError] = useState("");

  async function createTag() {
    if (!newTag.trim()) return;
    setError("");
    try {
      await api.post("/api/tags", { name: newTag.trim() });
      setNewTag("");
      onChanged();
    } catch (err) {
      setError(err.response?.data?.message || "Tag already exists");
    }
  }

  async function deleteTag(id) {
    await api.delete(`/api/tags/${id}`);
    onChanged();
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-box" onClick={e => e.stopPropagation()}>
        <h2>Manage Tags</h2>
        <div className="tag-create-row">
          <input
            type="text"
            placeholder="New tag name"
            value={newTag}
            onChange={e => setNewTag(e.target.value)}
            onKeyDown={e => e.key === "Enter" && createTag()}
          />
          <button className="btn-primary" onClick={createTag}>Add</button>
        </div>
        {error && <p className="modal-error">{error}</p>}
        <div className="tag-list">
          {tags.length === 0 && <p className="empty-tags">No tags yet.</p>}
          {tags.map(tag => (
            <div key={tag.id} className="tag-row">
              <span className="tag-chip">{tag.name}</span>
              <button className="btn-sm btn-danger" onClick={() => deleteTag(tag.id)}>Remove</button>
            </div>
          ))}
        </div>
        <div className="modal-actions">
          <button className="btn-ghost" onClick={onClose}>Close</button>
        </div>
      </div>
    </div>
  );
}