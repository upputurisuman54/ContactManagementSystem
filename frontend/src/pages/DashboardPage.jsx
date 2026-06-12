import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import { useAuth } from "../AuthContext";
import "../styles/dashboard.css";

export default function DashboardPage() {
  const { userName } = useAuth();
  const navigate = useNavigate();
  const [stats, setStats] = useState(null);

  useEffect(() => {
    api.get("/api/contacts/stats").then(res => setStats(res.data));
  }, []);

  if (!stats) return <div className="dashboard-loading">Loading...</div>;

  const maxCompany = stats.topCompanies.length > 0 ? stats.topCompanies[0].count : 1;
  const maxTag = stats.contactsPerTag.length > 0 ? stats.contactsPerTag[0].count : 1;

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <div>
          <h1>Dashboard</h1>
          <span className="dash-welcome">Hi, {userName}</span>
        </div>
        <button className="btn-outline" onClick={() => navigate("/contacts")}>← Contacts</button>
      </header>

      <div className="stats-grid">
        <div className="stat-card">
          <span className="stat-number">{stats.totalContacts}</span>
          <span className="stat-label">Total Contacts</span>
        </div>
        <div className="stat-card accent">
          <span className="stat-number">{stats.favouriteCount}</span>
          <span className="stat-label">Favourites</span>
        </div>
        <div className="stat-card">
          <span className="stat-number">{stats.tagCount}</span>
          <span className="stat-label">Tags</span>
        </div>
        <div className="stat-card">
          <span className="stat-number">
            {stats.totalContacts > 0
              ? Math.round((stats.favouriteCount / stats.totalContacts) * 100)
              : 0}%
          </span>
          <span className="stat-label">Favourite Rate</span>
        </div>
      </div>

      <div className="charts-row">
        {stats.topCompanies.length > 0 && (
          <div className="chart-card">
            <h3>Top Companies</h3>
            <div className="bar-chart">
              {stats.topCompanies.map((item, i) => (
                <div key={i} className="bar-row">
                  <span className="bar-label">{item.company}</span>
                  <div className="bar-track">
                    <div
                      className="bar-fill"
                      style={{ width: `${(item.count / maxCompany) * 100}%` }}
                    />
                  </div>
                  <span className="bar-value">{item.count}</span>
                </div>
              ))}
            </div>
          </div>
        )}

        {stats.contactsPerTag.length > 0 && (
          <div className="chart-card">
            <h3>Contacts by Tag</h3>
            <div className="bar-chart">
              {stats.contactsPerTag.map((item, i) => (
                <div key={i} className="bar-row">
                  <span className="bar-label">{item.tag}</span>
                  <div className="bar-track">
                    <div
                      className="bar-fill tag-bar"
                      style={{ width: `${(item.count / maxTag) * 100}%` }}
                    />
                  </div>
                  <span className="bar-value">{item.count}</span>
                </div>
              ))}
            </div>
          </div>
        )}

        {stats.topCompanies.length === 0 && stats.contactsPerTag.length === 0 && (
          <div className="chart-card empty-chart">
            <p>Add contacts with companies and tags to see charts here.</p>
          </div>
        )}
      </div>
    </div>
  );
}