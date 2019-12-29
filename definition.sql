-- SQLite database definition used in the application

CREATE TABLE IF NOT EXISTS member (
       member_id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
       full_name  TEXT    NOT NULL,
       birth_date TEXT,  -- ISO8601 string: "YYYY-MM-DD HH:MM:SS.SSS"
       role       INTEGER DEFAULT 0, -- Enum(ORDINARY, ACCOUNTANT, CHIEF)
       pass_key   TEXT    DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS account (
       account_id   INTEGER PRIMARY KEY NOT NULL,
       account_name TEXT    NOT NULL,
       owner_id     INTEGER NOT NULL,
       balance      INTEGER NOT NULL,
       FOREIGN KEY (owner_id) REFERENCES member(member_id)
         ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS transaction_ (
       tx_id       INTEGER PRIMARY KEY NOT NULL,
       account_id  INTEGER NOT NULL,
       amount      INTEGER NOT NULL,
       date_       TEXT NOT NULL,  -- ISO8601 string: "YYYY-MM-DD HH:MM:SS.SSS"
       description TEXT,
       FOREIGN KEY (account_id) REFERENCES account(account_id)
         ON DELETE SET NULL
);