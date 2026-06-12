import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../api";
import "../styles/contactform.css";

export default function ContactFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = Boolean(id);

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [company, setCompany] = useState("");
  const [favourite, setFavourite] = useState(false);
  const [photo, setPhoto] = useState(null);
  const [photoPreview, setPhotoPreview] = useState(null);
  const [existingPhoto, setExistingPhoto] = useState(null);
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (!isEdit) return;
    api.get(`/api/contacts/${id}`).then(res => {
      const c = res.data;
      setName(c.name || "");
      setEmail(c.email || "");
      setPhone(c.phone || "");
      setCompany(c.company || "");
      setFavourite(c.favourite || false);
      if (c.photoUrl) setExistingPhoto(`http://localhost:8080${c.photoUrl}`);
    });
  }, [id, isEdit]);

  function handlePhotoChange(e) {
    const file = e.target.files[0];
    if (!file) return;
    setPhoto(file);
    setPhotoPreview(URL.createObjectURL(file));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setSaving(true);
    try {
      const payload = { name, email, phone, company, favourite };
      let savedId = id;

      if (isEdit) {
        await api.put(`/api/contacts/${id}`, payload);
      } else {
        const res = await api.post("/api/contacts", payload);
        savedId = res.data.id;
      }

      if (photo) {
        const form = new FormData();
        form.append("file", photo);
        await api.post(`/api/contacts/${savedId}/photo`, form);
      }

      navigate("/contacts");
    } catch {
      setError("Failed to save contact. Please try again.");
    } finally {
      setSaving(false);
    }
  }

  const avatar = photoPreview || existingPhoto;

  return (
    <div className="cf-page">
      <div className="cf-container">

        <div className="cf-back" onClick={() => navigate("/contacts")}>
          ← Back to Contacts
        </div>

        <div className="cf-card">
          <div className="cf-left">
            <div className="cf-avatar-wrap">
              {avatar ? (
                <img src={avatar} alt="preview" className="cf-avatar-img" />
              ) : (
                <div className="cf-avatar-placeholder">
                  {name ? name.charAt(0).toUpperCase() : "?"}
                </div>
              )}
              <label className="cf-photo-btn">
                {avatar ? "Change photo" : "Add photo"}
                <input type="file" accept="image/*" onChange={handlePhotoChange} hidden />
              </label>
            </div>

            <div className="cf-left-info">
              <h2>{isEdit ? "Edit Contact" : "New Contact"}</h2>
              <p>{isEdit ? "Update the details below" : "Fill in the details to add a new contact"}</p>
            </div>
          </div>

          <div className="cf-right">
            {error && <div className="cf-error">{error}</div>}

            <form onSubmit={handleSubmit} className="cf-form">
              <div className="cf-field">
                <label>Name <span className="cf-required">*</span></label>
                <input
                  type="text"
                  placeholder="Full name"
                  value={name}
                  onChange={e => setName(e.target.value)}
                  required
                />
              </div>

              <div className="cf-field">
                <label>Email</label>
                <input
                  type="email"
                  placeholder="email@example.com"
                  value={email}
                  onChange={e => setEmail(e.target.value)}
                />
              </div>

              <div className="cf-field">
                <label>Phone</label>
                <input
                  type="text"
                  placeholder="+91 00000 00000"
                  value={phone}
                  onChange={e => setPhone(e.target.value)}
                />
              </div>

              <div className="cf-field">
                <label>Company</label>
                <input
                  type="text"
                  placeholder="Company name"
                  value={company}
                  onChange={e => setCompany(e.target.value)}
                />
              </div>

              <div className="cf-favourite">
                <label className="cf-toggle">
                  <input
                    type="checkbox"
                    checked={favourite}
                    onChange={e => setFavourite(e.target.checked)}
                  />
                  <span className="cf-toggle-track">
                    <span className="cf-toggle-thumb" />
                  </span>
                  <span className="cf-toggle-label">Mark as favourite</span>
                </label>
              </div>

              <div className="cf-actions">
                <button type="button" className="cf-btn-cancel" onClick={() => navigate("/contacts")}>
                  Cancel
                </button>
                <button type="submit" className="cf-btn-save" disabled={saving}>
                  {saving ? "Saving..." : isEdit ? "Save changes" : "Add contact"}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}