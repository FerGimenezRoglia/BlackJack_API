-- Seleccionar la base de datos
CREATE DATABASE IF NOT EXISTS blackjackDB;
USE blackjackDB;

-- Tabla de jugadores
CREATE TABLE IF NOT EXISTS player (
    id BINARY(16) PRIMARY KEY,  -- UUID en formato binario
    name VARCHAR(150) NOT NULL,
    total_games INT DEFAULT 0,
    total_wins INT DEFAULT 0
);

-- Tabla de partidas
CREATE TABLE IF NOT EXISTS game (
    id BINARY(16) PRIMARY KEY,  -- UUID en formato binario
    status ENUM('IN_PROGRESS', 'FINISHED') NOT NULL,
    winner VARCHAR(150) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla intermedia para relacionar jugadores y partidas (muchos a muchos)
CREATE TABLE IF NOT EXISTS player_game (
    player_id BINARY(16) NOT NULL,
    game_id BINARY(16) NOT NULL,
    PRIMARY KEY (player_id, game_id),
    FOREIGN KEY (player_id) REFERENCES player(id) ON DELETE CASCADE,
    FOREIGN KEY (game_id) REFERENCES game(id) ON DELETE CASCADE
);

-- Tabla para almacenar cartas usadas en cada partida
CREATE TABLE IF NOT EXISTS game_card (
    id BINARY(16) PRIMARY KEY,  -- UUID en formato binario
    game_id BINARY(16) NOT NULL,
    suit ENUM('HEART', 'DIAMOND', 'CLUB', 'SPADE') NOT NULL,
    card_value ENUM('TWO', 'THREE', 'FOUR', 'FIVE', 'SIX', 'SEVEN', 'EIGHT', 'NINE', 'TEN', 'JACK', 'QUEEN', 'KING', 'ACE') NOT NULL,
    FOREIGN KEY (game_id) REFERENCES game(id) ON DELETE CASCADE
);
