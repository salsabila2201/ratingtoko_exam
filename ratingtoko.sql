-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 31 Jan 2024 pada 06.38
-- Versi server: 10.4.28-MariaDB
-- Versi PHP: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ratingtoko`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `seller_rating`
--

CREATE TABLE `seller_rating` (
  `id` int(11) NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `seller_name` varchar(100) NOT NULL,
  `rating` int(11) NOT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `seller_rating`
--

INSERT INTO `seller_rating` (`id`, `customer_name`, `seller_name`, `rating`, `created_at`) VALUES
(1, 'Salsabila', 'toko Mainan', 1, '2024-01-30 21:22:05'),
(2, 'Bila', 'Toko Makanan', 4, '2024-01-31 09:02:07'),
(3, 'Rani', 'Toko Makanan', 3, '2024-01-31 09:02:29'),
(4, 'Nur', 'Toko Makanan', 2, '2024-01-31 11:37:46'),
(5, 'Husna', 'Toko Makanan', 3, '2024-01-31 11:38:01');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `seller_rating`
--
ALTER TABLE `seller_rating`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `seller_rating`
--
ALTER TABLE `seller_rating`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
