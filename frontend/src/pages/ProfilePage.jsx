import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import { useAuth } from "../AuthContext";
import "../styles/profile.css";

export default function ProfilePage() {
  const { userName, login } = useAuth();
  const navigate = useNavigate();

  const [profile, setProfile] = useState({ name: "", email: "" });
  const [nameInput, setNameInput] = useState("");
  const [nameMsg, setNameMsg] = useState("");

  const [passwords, setPasswords] = useState({ currentPassword: "", newPassword: "", confirm: "" });
  const [passMsg, setPassMsg] = useState("");
  const [passError, setPassError] = useState("");

  useEffect(() => {
    api.get("/api/profile").then(res => {
      setProfile(res.data);
      setNameInput(res.data.name);
    });
  }, []);

  async function handleNameSave() {
    setNameMsg("");
    await api.put("/api/profile/name", { name: nameInput });
    setProfile(p => ({ ...p, name: nameInput }));
    login(localStorage.getItem("token"), nameInput);
    setNameMsg("Name updated.");
    setTimeout(() => setNameMsg(""), 3000);
  }

  async function handlePasswordSave() {
    setPassMsg("");
    setPassError("");
    if (passwords.newPassword !== passwords.confirm) {
      setPassError("New passwords do not match.");
      return;
    }
    if (passwords.newPassword.length < 6) {
      setPassError("New password must be at least 6 characters.");
      return;
    }
    try {
      await api.put("/api/profile/password", {
        currentPassword: passwords.currentPassword,
        newPassword: passwords.newPassword,
      });
      setPasswords({ currentPassword: "", newPassword: "", confirm: "" });
      setPassMsg("Password changed successfully.");
      setTimeout(() => setPassMsg(""), 3000);
    } catch (err) {
      setPassError(err.response?.data?.message || "Current password is incorrect.");
    }
  }

  return (
    <div className="profile-page">
      <div className="profile-header">
        <button className="back-btn" onClick={() => navigate("/contacts")}>← Back</button>
        <h2>Profile</h2>
      </div>

      <div className="profile-card">
        <h3>Account Info</h3>
        <div className="profile-field">
          <label>Email</label>
          <input type="text" value={profile.email} disabled />
        </div>
        <div className="profile-field">
          <label>Display Name</label>
          <input
            type="text"
            value={nameInput}
            onChange={e => setNameInput(e.target.value)}
          />
        </div>
        {nameMsg && <div className="profile-success">{nameMsg}</div>}
        <button className="profile-btn" onClick={handleNameSave}>Save Name</button>
      </div>

      <div className="profile-card">
        <h3>Change Password</h3>
        <div className="profile-field">
          <label>Current Password</label>
          <input
            type="password"
            value={passwords.currentPassword}
            onChange={e => setPasswords({ ...passwords, currentPassword: e.target.value })}
            placeholder="••••••••"
          />
        </div>
        <div className="profile-field">
          <label>New Password</label>
          <input
            type="password"
            value={passwords.newPassword}
            onChange={e => setPasswords({ ...passwords, newPassword: e.target.value })}
            placeholder="••••••••"
          />
        </div>
        <div className="profile-field">
          <label>Confirm New Password</label>
          <input
            type="password"
            value={passwords.confirm}
            onChange={e => setPasswords({ ...passwords, confirm: e.target.value })}
            placeholder="••••••••"
          />
        </div>
        {passError && <div className="profile-error">{passError}</div>}
        {passMsg && <div className="profile-success">{passMsg}</div>}
        <button className="profile-btn" onClick={handlePasswordSave}>Change Password</button>
      </div>
    </div>
  );
}