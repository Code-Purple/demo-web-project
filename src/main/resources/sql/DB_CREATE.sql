 --CREATE DATABASE IF NOT EXISTS MainDb;
 --Use MainDb;


CREATE TABLE IF NOT EXISTS Song
(
	SongID serial PRIMARY KEY,
	Name varchar(255) NOT NULL,
	Artist varchar(255) NOT NULL,
	MP3Url varchar(255),
	VideoUrl varchar(255),
	BPM int NOT NULL,
	GAP int NOT NULL,
	LyricText text
);

CREATE TABLE IF NOT EXISTS SongNoteType
(
	SongNoteTypeID int PRIMARY KEY,
	Name varchar(30) NOT NULL,
	PointModifierE2 int NOT NULL
);

CREATE TABLE IF NOT EXISTS SongNote
(
	SongNoteID serial PRIMARY KEY,
	SequenceNum int NOT NULL,
	StartingBeat int NOT NULL,
	TypeID int NOT NULL REFERENCES SongNoteType(SongNoteTypeID) ON DELETE SET NULL,
	Duration int NOT NULL,
	Pitch int NOT NULL,
	SongID int NOT NULL REFERENCES Song(SongID) ON DELETE SET NULL,
	NoteText varchar(255) NOT NULL, 
	Constraint song_unique_sequence UNIQUE(SongID, SequenceNum)
);

CREATE TABLE IF NOT EXISTS UserTable
(
	UserID serial PRIMARY KEY,
	Username varchar(50) UNIQUE NOT NULL,
	Password varchar(50) NOT NULL 
);

CREATE TABLE IF NOT EXISTS Match
(
	MatchID serial PRIMARY KEY,
	HostID int NOT NULL REFERENCES UserTable(UserID) ON DELETE Set NULL,
	InviteeID int NOT NULL REFERENCES UserTable(UserID) ON DELETE Set NULL

);

CREATE TABLE IF NOT EXISTS UserScore
(
	ScoreID serial PRIMARY KEY,
	UserID int NOT NULL REFERENCES UserTable(UserID) ON DELETE SET NULL,
	Score int NOT NULL, 
	Singleplayer boolean NOT NULL,
	MatchID int REFERENCES Match(MatchID)
);

INSERT INTO SongNoteType values
	(0,'Regular', 1.0),
	(1, 'Golden', 2.0),
	(2, 'Freestyle', 0.0),
	(3, 'LineBreak', 0.0);