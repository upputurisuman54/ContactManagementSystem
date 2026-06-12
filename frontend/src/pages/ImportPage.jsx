import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import "../styles/ImportPage.css";

export default function ImportPage() {
  const navigate = useNavigate();
  const [file, setFile] = useState(null);
  const [importing, setImporting] = useState(false);
  const [progress, setProgress] = useState(0);
  const [done, setDone] = useState(false);
  const [error, setError] = useState("");

  const pickFile = (selected) => {
    if (!selected || !selected.name.endsWith(".csv")) {
      setError("Only .csv files are accepted.");
      return;
    }
    setError("");
    setDone(false);
    setProgress(0);
    setFile(selected);
  };

  const removeFile = () => {
    setFile(null);
    setDone(false);
    setProgress(0);
    setError("");
  };

  const startImport = async () => {
    if (!file) return;
    setImporting(true);
    setProgress(0);
    setDone(false);
    setError("");

    const formData = new FormData();
    formData.append("file", file);

    try {
      await api.post("/api/contacts/import", formData, {
        headers: { "Content-Type": "multipart/form-data" },
        onUploadProgress: (e) => {
          setProgress(Math.round((e.loaded * 100) / e.total));
        },
      });
      setProgress(100);
      setDone(true);
    } catch (err) {
      setError("Import failed. Check your CSV format and try again.");
    } finally {
      setImporting(false);
    }
  };

  const downloadTemplate = () => {
    const csv = "Name,Email,Phone,Company,Favourite,Tags\nJohn Doe,john@example.com,+91 9999999999,Acme Corp,false,friend";
    const blob = new Blob([csv], { type: "text/csv" });
    const a = document.createElement("a");
    a.href = URL.createObjectURL(blob);
    a.download = "contacts_template.csv";
    a.click();
  };

  return (
    <div className="import-page">
      <div className="import-container">
        <button className="back-btn" onClick={() => navigate(-1)}>
          Back
        </button>

        <div className="import-header">
          <h1>Import contacts</h1>
          <p>Upload a CSV file to bulk import contacts into your account.</p>
        </div>

        <div className="format-card">
          <div className="format-left">
            <div>
              <p className="format-label">Required CSV format</p>
              <code className="format-code">Name, Email, Phone, Company, Favourite, Tags</code>
            </div>
          </div>
          <button className="template-btn" onClick={downloadTemplate}>
            Download template
          </button>
        </div>

        <div className="drop-zone" onClick={() => document.getElementById("csv-input").click()}>
          <input
            id="csv-input"
            type="file"
            accept=".csv"
            style={{ display: "none" }}
            onChange={(e) => pickFile(e.target.files[0])}
          />

          {!file && (
            <div>
              <p className="drop-title">Drop your CSV file here</p>
              <p className="drop-sub">or click to browse</p>
            </div>
          )}

          {file && !done && (
            <div>
              <p className="drop-title">File ready to import</p>
              <p className="drop-sub">{file.name}</p>
              <button className="remove-btn" onClick={removeFile}>Remove</button>
            </div>
          )}

          {done && (
            <div>
              <p className="drop-title">Import complete</p>
              <p className="drop-sub">{file.name}</p>
            </div>
          )}
        </div>

        {error && <div className="error-box">{error}</div>}

        {(importing || done) && (
          <div className="progress-wrap">
            <div className="progress-top">
              <span>{done ? "Done" : "Importing..."}</span>
              <span>{progress}%</span>
            </div>
            <div className="progress-track">
              <div className="progress-fill" style={{ width: progress + "%" }} />
            </div>
          </div>
        )}

        {done && <div className="success-box">{file.name} imported successfully.</div>}

        <button className="import-btn" onClick={startImport} disabled={!file || importing || done}>
          {importing ? "Importing..." : done ? "Import complete" : "Import contacts"}
        </button>

        <div className="tips">
          <p className="tips-heading">Tips</p>
          <p>First row must be the header row matching the format above</p>
          <p>Favourite column accepts true or false</p>
          <p>Tags column accepts comma separated values</p>
          <p>Duplicate emails will be skipped automatically</p>
        </div>
      </div>
    </div>
  );
}