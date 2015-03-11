--CREATE DATABASE MainDb;


CREATE TABLE IF NOT EXISTS Song
(
	SongID int IDENTITY PRIMARY KEY,
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
	SongNoteTypeID int IDENTITY PRIMARY KEY,
	Name varchar(30) NOT NULL,
	PointModifierE2 int NOT NULL
);

CREATE TABLE IF NOT EXISTS SongNote
(
	SongNoteID int IDENTITY PRIMARY KEY,
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
	UserID int IDENTITY PRIMARY KEY,
	Username varchar(50) UNIQUE NOT NULL,
	Password varchar(50) NOT NULL 
);

CREATE TABLE IF NOT EXISTS Match
(
	MatchID int IDENTITY PRIMARY KEY,
	HostID int NOT NULL REFERENCES UserTable(UserID) ON DELETE Set NULL,
	InviteeID int NOT NULL REFERENCES UserTable(UserID) ON DELETE Set NULL

);

CREATE TABLE IF NOT EXISTS UserScore
(
	ScoreID int IDENTITY PRIMARY KEY,
	UserID int NOT NULL REFERENCES UsserTable(UserID) ON DELETE SET NULL,
	Score int NOT NULL, 
	Singleplayer boolean NOT NULL,
	MatchID int REFERENCES Match(MatchID)
);