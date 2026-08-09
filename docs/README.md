# BTSDA video reference

`btsda_videos.csv` is the full export of Bristol Tang Soo Do Academy YouTube
videos (Title, URL), kept as the source of truth for video links. Titles are
verbatim from the channel (including spellings like "Niahanchi" / "Sip Soo").

## Wired into the app
- **One-steps** — `Il Soo Sik Dae Ryun (1 Step Sparring)` 1‑5 … 26‑30 and
  `Bo Staff 1 Steps` 1‑5 … 26‑30 → `data/Models.kt` (`VideoLinks`).
- **Forms** — the empty-hand hyung and the three `Bong Hyung` bo staff forms
  → `data/Forms.kt` (`FormVideoLinks`).

## In the list but not yet in the app
- **Naihanchi E Dan** and **Naihanchi Sam Dan** — higher (Dan-grade) forms; no
  belt assigned in the app yet.
- **"… - Side view"** alternate angles for the Pyung Ahn forms — the app links
  the main front-view video only.

To add any of these, map them to a belt/rank and add the id to the relevant
links table.
