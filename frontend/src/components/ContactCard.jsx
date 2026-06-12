import { useState } from "react";
import api from "../api";

export default function ContactCard({ contact, tags, onEdit, onDelete, onToggleFavourite, onNotes, onTagsUpdated }) {
  const [assigningTags, setAssigningTags] = useState(false);
  const [selectedTagIds, setSelectedTagIds] = useState(contact.tags?.map(t => t.id) || []);

  function toggleTagSelection(tagId) {
    setSelectedTagIds(prev =>
      prev.includes(tagId) ? prev.filter(id => id !== tagId) : [...prev, tagId]
    );
  }

  async function saveTags() {
    await api.put(`/api/contacts/${contact.id}/tags`, selectedTagIds);
    setAssigningTags(false);
    onTagsUpdated();
  }

  return (
    <div className="contact-card">
      <div className="card-top">
        {contact.photoUrl ? (
          <img
            src={`http://localhost:8080${contact.photoUrl}`}
            alt={contact.name}
            className="contact-avatar"
          />
        ) : (
          <div className="contact-avatar placeholder">
            {contact.name?.charAt(0).toUpperCase()}
          </div>
        )}
        <div className="card-info">
          <h3>{contact.name}</h3>
          {contact.company && <span className="card-company">{contact.company}</span>}
        </div>
        <button
          className={`fav-btn ${contact.favourite ? "active" : ""}`}
          onClick={onToggleFavourite}
        >
          {contact.favourite ? "★" : "☆"}
        </button>
      </div>

      <div className="card-details">
        {contact.email && <p>{contact.email}</p>}
        {contact.phone && <p>{contact.phone}</p>}
      </div>

      {contact.tags?.length > 0 && (
        <div className="card-tags">
          {contact.tags.map(tag => (
            <span key={tag.id} className="tag-chip">{tag.name}</span>
          ))}
        </div>
      )}

      {assigningTags && (
        <div className="tag-assign-panel">
          <div className="tag-options">
            {tags.length === 0 && <span className="no-tags-hint">No tags yet. Create tags first.</span>}
            {tags.map(tag => (
              <label key={tag.id} className="tag-option">
                <input
                  type="checkbox"
                  checked={selectedTagIds.includes(tag.id)}
                  onChange={() => toggleTagSelection(tag.id)}
                />
                {tag.name}
              </label>
            ))}
          </div>
          <div className="tag-assign-actions">
            <button className="btn-sm btn-primary" onClick={saveTags}>Save</button>
            <button className="btn-sm btn-ghost" onClick={() => setAssigningTags(false)}>Cancel</button>
          </div>
        </div>
      )}

      <div className="card-actions">
        <button className="btn-sm btn-outline" onClick={onEdit}>Edit</button>
        <button className="btn-sm btn-outline" onClick={() => setAssigningTags(v => !v)}>Tags</button>
        <button className="btn-sm btn-outline" onClick={onNotes}>Notes</button>
        <button className="btn-sm btn-danger" onClick={onDelete}>Delete</button>
      </div>
    </div>
  );
}