import { useState, useEffect, useCallback, useRef } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import { useAuth } from "../AuthContext";
import ContactCard from "../components/ContactCard";
import NotesPanel from "../components/NotesPanel";
import TagManager from "../components/TagManager";
import "../styles/contacts.css";

export default function ContactsPage() {
  const { userName, logout } = useAuth();
  const navigate = useNavigate();

  const [contacts, setContacts] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [page, setPage] = useState(0);
  const [tags, setTags] = useState([]);
  const [activeTag, setActiveTag] = useState(null);
  const [showFavourites, setShowFavourites] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [searchResults, setSearchResults] = useState(null);
  const [notesContact, setNotesContact] = useState(null);
  const [showTagManager, setShowTagManager] = useState(false);
  const searchTimer = useRef(null);

  const loadContacts = useCallback(async () => {
    try {
      const params = { page, size: 10 };
      if (activeTag) params.tagId = activeTag;
      const res = await api.get("/api/contacts", { params });
      setContacts(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch {
      console.error("Failed to load contacts");
    }
  }, [page, activeTag]);

  const loadTags = async () => {
    try {
      const res = await api.get("/api/tags");
      setTags(res.data);
    } catch {
      console.error("Failed to load tags");
    }
  };

  useEffect(() => {
    loadTags();
  }, []);

  useEffect(() => {
    if (!showFavourites && !searchResults) loadContacts();
  }, [loadContacts, showFavourites, searchResults]);

  function handleSearchChange(e) {
    const val = e.target.value;
    setSearchTerm(val);
    clearTimeout(searchTimer.current);
    if (!val.trim()) {
      setSearchResults(null);
      return;
    }
    searchTimer.current = setTimeout(async () => {
      const res = await api.get("/api/contacts/search", { params: { keyword: val } });
      setSearchResults(res.data);
    }, 300);
  }

  async function loadFavourites() {
    setShowFavourites(true);
    setSearchResults(null);
    setSearchTerm("");
    const res = await api.get("/api/contacts/favourites");
    setContacts(res.data);
    setTotalPages(0);
  }

  function clearFilters() {
    setShowFavourites(false);
    setSearchResults(null);
    setSearchTerm("");
    setActiveTag(null);
    setPage(0);
  }

  async function handleDelete(id) {
    await api.delete(`/api/contacts/${id}`);
    loadContacts();
  }

  async function handleToggleFavourite(contact) {
    await api.put(`/api/contacts/${contact.id}`, {
      name: contact.name,
      email: contact.email,
      phone: contact.phone,
      company: contact.company,
      favourite: !contact.favourite,
    });
    if (showFavourites) loadFavourites();
    else loadContacts();
  }

  async function handleExport() {
    const res = await api.get("/api/contacts/export", { responseType: "blob" });
    const url = window.URL.createObjectURL(new Blob([res.data]));
    const a = document.createElement("a");
    a.href = url;
    a.download = "contacts.csv";
    a.click();
    window.URL.revokeObjectURL(url);
  }

  const displayed = searchResults ?? contacts;

  return (
    <div className="contacts-page">
      <header className="contacts-header">
        <div className="header-left">
          <h1>Contacts</h1>
          <span className="welcome">Hi, {userName}</span>
        </div>
        <div className="header-actions">
          <button className="btn-outline" onClick={() => navigate("/dashboard")}>Dashboard</button>
          <button className="btn-outline" onClick={() => navigate("/activity")}>Activity</button>
          <button className="btn-outline" onClick={() => navigate("/profile")}>Profile</button>
          <button className="btn-outline" onClick={() => setShowTagManager(true)}>Tags</button>
          <button className="btn-outline" onClick={handleExport}>Export CSV</button>
          <button className="btn-outline" onClick={() => navigate("/import")}>Import CSV</button>
          <button className="btn-primary" onClick={() => navigate("/contacts/new")}>+ Add Contact</button>
          <button className="btn-ghost" onClick={logout}>Logout</button>
        </div>
      </header>

      <div className="contacts-toolbar">
        <input
          className="search-input"
          type="text"
          placeholder="Search by name, email, company..."
          value={searchTerm}
          onChange={handleSearchChange}
        />
        <div className="filter-row">
          <button
            className={`filter-btn ${!showFavourites && !activeTag && !searchResults ? "active" : ""}`}
            onClick={clearFilters}
          >
            All
          </button>
          <button
            className={`filter-btn ${showFavourites ? "active" : ""}`}
            onClick={loadFavourites}
          >
            ★ Favourites
          </button>
          {tags.map(tag => (
            <button
              key={tag.id}
              className={`filter-btn tag-btn ${activeTag === tag.id ? "active" : ""}`}
              onClick={() => {
                setActiveTag(tag.id);
                setShowFavourites(false);
                setSearchResults(null);
                setSearchTerm("");
                setPage(0);
              }}
            >
              {tag.name}
            </button>
          ))}
        </div>
      </div>

      <div className="contacts-grid">
        {displayed.length === 0 && (
          <div className="empty-state">No contacts found.</div>
        )}
        {displayed.map(contact => (
          <ContactCard
            key={contact.id}
            contact={contact}
            tags={tags}
            onEdit={() => navigate(`/contacts/edit/${contact.id}`)}
            onDelete={() => handleDelete(contact.id)}
            onToggleFavourite={() => handleToggleFavourite(contact)}
            onNotes={() => setNotesContact(contact)}
            onTagsUpdated={loadContacts}
          />
        ))}
      </div>

      {!showFavourites && !searchResults && totalPages > 1 && (
        <div className="pagination">
          {Array.from({ length: totalPages }, (_, i) => (
            <button
              key={i}
              className={`page-btn ${page === i ? "active" : ""}`}
              onClick={() => setPage(i)}
            >
              {i + 1}
            </button>
          ))}
        </div>
      )}

      {notesContact && (
        <NotesPanel
          contact={notesContact}
          onClose={() => setNotesContact(null)}
        />
      )}

      {showTagManager && (
        <TagManager
          tags={tags}
          onClose={() => setShowTagManager(false)}
          onChanged={loadTags}
        />
      )}
    </div>
  );
}