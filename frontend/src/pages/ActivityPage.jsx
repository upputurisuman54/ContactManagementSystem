import { useEffect, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import "../styles/activity.css";

const ACTION_LABELS = {
  CREATED: "Created",
  UPDATED: "Updated",
  DELETED: "Deleted",
  PHOTO_CHANGED: "Photo Changed",
  TAGGED: "Tags Updated",
  NOTE_ADDED: "Note Added",
};

function timeAgo(isoString) {
  const diff = Date.now() - new Date(isoString).getTime();
  const mins = Math.floor(diff / 60000);
  if (mins < 1) return "just now";
  if (mins < 60) return `${mins}m ago`;
  const hrs = Math.floor(mins / 60);
  if (hrs < 24) return `${hrs}h ago`;
  const days = Math.floor(hrs / 24);
  if (days < 7) return `${days}d ago`;
  return new Date(isoString).toLocaleDateString();
}

export default function ActivityPage() {
  const [entries, setEntries] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const navigate = useNavigate();

  const load = useCallback(async (p) => {
    const res = await api.get(`/api/activity?page=${p}`);
    setEntries(res.data.content);
    setTotalPages(res.data.totalPages);
  }, []);

  useEffect(() => {
    load(page);
  }, [page, load]);

  return (
    <div className="activity-page">
      <div className="activity-header">
        <button className="back-btn" onClick={() => navigate("/contacts")}>← Back</button>
        <h2>Activity Log</h2>
      </div>

      {entries.length === 0 && <p className="no-activity">No activity yet.</p>}

      <div className="timeline">
        {entries.map((entry) => (
          <div key={entry.id} className="timeline-item">
            <div className={`action-badge action-${entry.action.toLowerCase()}`}>
              {ACTION_LABELS[entry.action]}
            </div>
            <div className="timeline-body">
              <span className="contact-name">{entry.contactName}</span>
              <span className="time-ago">{timeAgo(entry.timestamp)}</span>
            </div>
          </div>
        ))}
      </div>

      {totalPages > 1 && (
        <div className="activity-pagination">
          <button disabled={page === 0} onClick={() => setPage(p => p - 1)}>Prev</button>
          <span>{page + 1} / {totalPages}</span>
          <button disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>Next</button>
        </div>
      )}
    </div>
  );
}