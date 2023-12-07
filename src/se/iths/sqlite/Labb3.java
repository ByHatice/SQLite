package se.iths.sqlite;

import java.sql.*;
import java.util.Scanner;

public class Labb3 {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        boolean quit = false;
        printActions();
        while (!quit) {
            System.out.println("\nYou are in the main menu. What would you like to do next?\nTo display the main menu again, press 0: ");
            String choiceMenu = scanner.nextLine();

            if (choiceMenu.matches("\\d+")) {
                int menuChoice = Integer.parseInt(choiceMenu);

                switch (menuChoice) {
                    case 0:
                        printActions();
                        break;

                    case 1:
                        showContent();
                        break;

                    case 2:
                        insert();
                        break;

                    case 3:
                        update();
                        break;

                    case 4:
                        delete();
                        break;

                    case 5:
                        searchArtist();
                        break;

                    case 6:
                        markAsFavorite();
                        break;

                    case 7:
                       showFavorite();
                        break;

                    case 8:
                        mergedData();
                        break;

                    case 9:
                        statistics();
                        break;

                    default:
                        System.out.println("INVALID INPUT.\nEnter a valid number.");
                        break;
                }
            } else if (choiceMenu.equalsIgnoreCase("q")) {
                quit = true;
                scanner.close();
            } else {
                System.out.println("INVALID INPUT.\nEnter a valid number or 'Q' to quit.");
            }
        }


    }

    private static Connection connect() {
        String url = "jdbc:sqlite:C:/Users/46736/sqlite-tools-win-x64-3440200/SQLite/labb3.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    private static void printActions() {
        System.out.printf("n%-10s%n","Meny:");
        System.out.println("""
                1  - Show content
                2  - Insert
                3  - Update
                4  - Delete
                5  - Search artist
                6  - Mark artist as favorite
                7  - View favorite
                8  - View merged data
                9  - View statistics on price
                """);
    }

    private static void showTable(){
         try {Connection conn = connect();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            int tableNumber = 1;

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Table " + tableNumber + ": " + tableName);
                tableNumber++;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void selectAlbum() {
        String sql = "SELECT * FROM album";

        try {Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                System.out.println("Album ID: " + rs.getInt("albumId") + "\n" +
                        "Title: " + rs.getString("albumName")+  "\n" +
                        "Release Year: " + rs.getInt("albumReleaseYear") + "\n" +
                        "Price: " + rs.getInt("albumPrice") + ":-" + "\n" );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void selectArtist() {
        String sql = "SELECT*FROM artist";

        try {Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                System.out.println("Artist ID: " + rs.getInt("artistId") + "\n" +
                        "Artist: " + rs.getString("artistName") + "\n" +
                        "Birth year: " + rs.getInt("birthYear") + "\n");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void selectCategory() {
        String sql = "SELECT * FROM category";

        try {Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                System.out.println("Category ID: " + rs.getInt("categoryId") +  "\t" +
                        "Genre: " + rs.getString("genre"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void showContent() {
        int choiceTable = 0;
        while (choiceTable != 4) {
            System.out.println("\n" + "Enter table number for more DETAILS:");
            showTable();
            System.out.println("Enter 4 if you want to return");

            if (scanner.hasNextInt()) {
                choiceTable = scanner.nextInt();
                scanner.nextLine();

                switch (choiceTable) {
                    case 1:
                        selectAlbum();
                        break;
                    case 2:
                        selectArtist();
                        break;
                    case 3:
                        selectCategory();
                        break;
                    case 4:
                        System.out.println("Returning...");
                        break;
                    default:
                        System.out.println("Invalid input, enter 4 if you want to return");
                        break;
                }
            } else {
                System.out.println("Invalid input, enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private static void insertAlbum(String albumName, int albumReleaseYear, int albumPrice, int categoryId, int artistId) {
        String sql = "INSERT INTO album(albumName, albumReleaseYear, albumPrice, categoryId, artistId) VALUES(?,?,?,?,?)";

        try{Connection conn = connect();
            PreparedStatement insertAlbumStmt = conn.prepareStatement(sql);
            insertAlbumStmt.setString(1, albumName);
            insertAlbumStmt.setInt(2, albumReleaseYear);
            insertAlbumStmt.setInt(3, albumPrice);
            insertAlbumStmt.setInt(4, categoryId);
            insertAlbumStmt.setInt(5, artistId);
            insertAlbumStmt.executeUpdate();
            System.out.println("You have added a new album: " + albumName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void insertArtist(String artistName, int birthYear) {
        String sql = "INSERT INTO artist(artistName, birthYear) VALUES(?,?)";

        try{Connection conn = connect();
            PreparedStatement insertArtistStmt = conn.prepareStatement(sql);
            insertArtistStmt.setString(1, artistName);
            insertArtistStmt.setInt(2, birthYear);

            insertArtistStmt.executeUpdate();
            System.out.println("You have added a new artist: " + artistName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void insertCategory(String genre) {
        String sql = "INSERT INTO category(genre) VALUES(?)";

        try{ Connection conn = connect();
            PreparedStatement insertCategoryStmt = conn.prepareStatement(sql);
            insertCategoryStmt.setString(1, genre);
            insertCategoryStmt.executeUpdate();
            System.out.println("You have added a new genre: " + genre);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void insert() {
        int choiceTable = 0;
        while (choiceTable != 4) {
            System.out.println("\n" + "Enter which table number you want to INSERT values into:");
            showTable();
            System.out.println("Enter 4 if you want to return");

            if (scanner.hasNextInt()) {
                choiceTable = scanner.nextInt();
                scanner.nextLine(); // Konsumera radbyte

                switch (choiceTable) {
                    case 1:
                        System.out.println("Enter album name: ");
                        String albumName = scanner.nextLine();
                        System.out.println("Enter album release year: ");
                        int albumReleaseYear = scanner.nextInt();
                        System.out.println("Enter album price: ");
                        int albumPrice = scanner.nextInt();
                        System.out.println("Enter category id: ");
                        int categoryId = scanner.nextInt();
                        System.out.println("Enter artist id: ");
                        int artistId = scanner.nextInt();
                        insertAlbum(albumName, albumReleaseYear, albumPrice, categoryId, artistId);
                        break;
                    case 2:
                        System.out.println("Enter artist name: ");
                        String artistName = scanner.nextLine();
                        System.out.println("Enter birth year: ");
                        int birthYear = scanner.nextInt();
                        insertArtist(artistName, birthYear);
                        break;
                    case 3:
                        System.out.println("Enter genre: ");
                        String genre = scanner.nextLine();
                        insertCategory(genre);
                        break;
                    case 4:
                        System.out.println("Returning...");
                        break;
                    default:
                        System.out.println("Invalid input, enter 4 if you want to return");
                        break;
                }
            } else {
                System.out.println("Invalid input, enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private static void updateAlbum(String albumName, int albumReleaseYear, int albumPrice, int categoryId, int artistId, int albumId) {
        String sql = "UPDATE album SET albumName = ?, albumReleaseYear = ?, albumPrice = ?, categoryId = ?, artistId = ? WHERE albumId = ?";

        try (Connection conn = connect();
             PreparedStatement updateAlbumStmt = conn.prepareStatement(sql)) {

            updateAlbumStmt.setString(1, albumName);
            updateAlbumStmt.setInt(2, albumReleaseYear);
            updateAlbumStmt.setInt(3, albumPrice);
            updateAlbumStmt.setInt(4, categoryId);
            updateAlbumStmt.setInt(5, artistId);
            updateAlbumStmt.setInt(6, albumId);

            updateAlbumStmt.executeUpdate();
            System.out.println("You have updated the album: " + albumName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void updateArtist(int artistId, String artistName, int birthYear) {
        String sql = "UPDATE artist SET artistName = ?, birthYear = ? WHERE artistId = ?";

        try (Connection conn = connect();
             PreparedStatement updateArtistStmt = conn.prepareStatement(sql)) {

            updateArtistStmt.setString(1, artistName);
            updateArtistStmt.setInt(2, birthYear);
            updateArtistStmt.setInt(3, artistId);

            updateArtistStmt.executeUpdate();
            System.out.println("You have updated the artist");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void updateCategory(String genre, int categoryId) {
        String sql = "UPDATE category SET genre = ? WHERE categoryId = ?";

        try (Connection conn = connect();
             PreparedStatement updateCategoryStmt = conn.prepareStatement(sql)) {

            updateCategoryStmt.setString(1, genre);
            updateCategoryStmt.setInt(2, categoryId);

            updateCategoryStmt.executeUpdate();
            System.out.println("You have updated the genre to: " + genre);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void update() {
        int choiceTable = 0;
        while (choiceTable != 4) {
            System.out.println("\n" + "Select the table number for UPDATING values:");
            showTable();
            System.out.println("Enter 4 if you want to return");

            if (scanner.hasNextInt()) {
                choiceTable = scanner.nextInt();
                scanner.nextLine();
                int categoryId;
                int artistId;

                switch (choiceTable) {
                    case 1:
                        System.out.println("Enter album id: ");
                        int albumId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter album name: ");
                        String albumName = scanner.nextLine();
                        System.out.println("Enter album release year: ");
                        int albumReleaseYear = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter album price: ");
                        int albumPrice = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter category id: ");
                        categoryId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter artist id: ");
                        artistId = scanner.nextInt();
                        scanner.nextLine();
                        updateAlbum(albumName, albumReleaseYear, albumPrice, categoryId, artistId, albumId);
                        break;
                    case 2:
                        System.out.println("Enter artist id: ");
                        artistId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter artist name: ");
                        String artistName = scanner.nextLine();
                        System.out.println("Enter birth year: ");
                        int birthYear = scanner.nextInt();
                        updateArtist(artistId, artistName, birthYear);
                        break;
                    case 3:
                        System.out.println("Enter category id: ");
                        categoryId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter genre: ");
                        String genre = scanner.nextLine();
                        updateCategory(genre, categoryId);
                        break;
                    case 4:
                        System.out.println("Returning...");
                        break;
                    default:
                        System.out.println("Invalid input, enter 4 if you want to return");
                        break;
                }
            } else {
                System.out.println("Invalid input, enter a valid number.");
                scanner.nextLine();
            }
        }

    }

    private static void deleteAlbum(int albumId) {
        String sql = "DELETE FROM album WHERE albumId = ?";

        try (Connection conn = connect();
             PreparedStatement deleteAlbumStmt = conn.prepareStatement(sql)) {

            deleteAlbumStmt.setInt(1, albumId);

            deleteAlbumStmt.executeUpdate();
            System.out.println("You have deleted the album");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void deleteArtist(int artistId) {
        String sql = "DELETE FROM artist WHERE artistId = ?";

        try (Connection conn = connect();
             PreparedStatement deleteArtistStmt = conn.prepareStatement(sql)) {

            deleteArtistStmt.setInt(1, artistId);

            deleteArtistStmt.executeUpdate();
            System.out.println("You have deleted the artist");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void deleteCategory(int categoryId) {
        String sql = "DELETE FROM category WHERE categoryId = ?";

        try (Connection conn = connect();
             PreparedStatement deleteCategoryStmt = conn.prepareStatement(sql)) {

            deleteCategoryStmt.setInt(1, categoryId);

            deleteCategoryStmt.executeUpdate();
            System.out.println("You have deleted the genre");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void delete() {
        int choiceTable = 0;
        while (choiceTable != 4) {
            System.out.println("\n" + "Select the table number for DELETING values:");
            showTable();
            System.out.println("Enter 4 if you want to return");

            if (scanner.hasNextInt()) {
                choiceTable = scanner.nextInt();
                scanner.nextLine();

                switch (choiceTable) {
                    case 1:
                        System.out.println("Enter album id: ");
                        int albumId = scanner.nextInt();
                        scanner.nextLine();
                        deleteAlbum(albumId);
                        break;
                    case 2:
                        System.out.println("Enter artist id: ");
                        int artistId = scanner.nextInt();
                        scanner.nextLine();
                        deleteArtist(artistId);
                        break;
                    case 3:
                        System.out.println("Enter category id: ");
                        int categoryId = scanner.nextInt();
                        scanner.nextLine();
                        deleteCategory(categoryId);
                        break;
                    case 4:
                        System.out.println("Returning...");
                        break;
                    default:
                        System.out.println("Invalid input, enter 4 if you want to return");
                        break;
                }
            } else {
                System.out.println("Invalid input, enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private static void searchArtist() {
        String sql = "SELECT * FROM artist WHERE artistName LIKE ?";

        try {Connection conn = connect();
            PreparedStatement searchArtistStmt = conn.prepareStatement(sql);
            System.out.println("Enter artist name: ");
            String artistName = scanner.nextLine();
            searchArtistStmt.setString(1, artistName);
            ResultSet rs = searchArtistStmt.executeQuery();

            System.out.printf("%-15s %-10s%n", "Artist", "Birth year");

            while (rs.next()) {
                System.out.printf("%-15s %-10s%n",
                        rs.getString("artistName"),
                        rs.getString("birthYear"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void markAsFavorite() {
        String sql = "UPDATE artist SET isFavorite = 1 WHERE artistName = ?;";

        try {Connection conn = connect();
            PreparedStatement markAsFavoriteStmt = conn.prepareStatement(sql);
            System.out.println("Enter artis name you want mark as favorite: ");
            String artistName = scanner.nextLine();
            markAsFavoriteStmt.setString(1, artistName);

            int rowsAffected = markAsFavoriteStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("You have marked " + artistName + " as favorite");
            } else {
                System.out.println("Artist with name " + artistName + " not found.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showFavorite() {
        String sql = "SELECT * FROM artist WHERE isFavorite = 1";

        try {Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.printf("%-15s %-10s%n", "Artist", "Birth year");

            while (rs.next()) {
                System.out.printf("%-15s %-10s%n",
                        rs.getString("artistName"),
                        rs.getString("birthYear"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void mergedData() {
        String sql = """
                SELECT album.albumName, artist.artistName, category.genre FROM album
                INNER JOIN artist ON album.artistId = artist.artistId
                INNER JOIN category ON album.categoryId = category.categoryId""";

        try {Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.printf("%-20s %-15s %-10s%n\n", "Album", "Artist", "Genre");

            while (rs.next()) {
                System.out.printf("%-20s %-15s %-10s%n",
                        rs.getString("albumName"),
                        rs.getString("artistName"),
                        rs.getString("genre"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void statistics() {
        String sql = "SELECT AVG(albumPrice) AS AveragePrice, MIN(albumPrice) AS MinPrice, MAX(albumPrice) AS MaxPrice FROM album";

        try {Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.printf("%-15s %-10s %-10s%n", "Average price", "Min price", "Max price");

            while (rs.next()) {
                System.out.printf("%-15s %-10s %-10s%n",
                        rs.getString("AveragePrice"),
                        rs.getString("MinPrice"),
                        rs.getString("MaxPrice"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}