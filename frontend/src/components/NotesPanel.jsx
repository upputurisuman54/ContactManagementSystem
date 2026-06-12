import { useState, useEffect, useCallback } from "react";
import api from "../api";
import "../styles/notes.css";

export default function NotesPanel({ contact, onClose }) {
  const [notes, setNotes] = useState([]);
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(true);

  const loadNotes = useCallback(async () => {
    try {
      const res = await api.get(`/api/contacts/${contact.id}/notes`);
      setNotes(res.data);
    } catch {
      console.error("Failed to load notes");
    } finally {
      setLoading(false);
    }
  }, [contact.id]);

  useEffect(() => {
    loadNotes();
  }, [loadNotes]);

  async function addNote() {
    if (!content.trim()) return;
    try {
      await api.post(`/api/contacts/${contact.id}/notes`, { content });
      setContent("");
      loadNotes();
    } catch {
      console.error("Failed to add note");
    }
  }

  async function deleteNote(noteId) {
    try {
      await api.delete(`/api/contacts/${contact.id}/notes/${noteId}`);
      loadNotes();
    } catch {
      console.error("Failed to delete note");
    }
  }

  function formatDate(dt) {
    return new Date(dt).toLocaleString();
  }

  return (
    <div className="notes-overlay" onClick={onClose}>
      <div className="notes-panel" onClick={e => e.stopPropagation()}>
        <div className="notes-header">
          <h3>Notes — {contact.name}</h3>
          <button className="close-btn" onClick={onClose}>✕</button>
        </div>

        <div className="notes-input-row">
          <textarea
            placeholder="Write a note..."
            value={content}
            onChange={e => setContent(e.target.value)}
            rows={3}
          />
          <button className="btn-primary" onClick={addNote}>Add</button>
        </div>

        <div className="notes-list">
          {loading && <p className="notes-empty">Loading...</p>}
          {!loading && notes.length === 0 && <p className="notes-empty">No notes yet.</p>}
          {notes.map(note => (
            <div key={note.id} className="note-item">
              <p className="note-content">{note.content}</p>
              <div className="note-meta">
                <span>{formatDate(note.createdAt)}</span>
                <button className="note-delete" onClick={() => deleteNote(note.id)}>Delete</button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}