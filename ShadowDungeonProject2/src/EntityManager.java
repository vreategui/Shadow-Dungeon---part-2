import bagel.Input;
import bagel.util.Point;

import java.util.*;

public class EntityManager {
    private final ArrayList<KeyBulletKin> keyBulletKin = new ArrayList<>();
    private final ArrayList<AshenEnemy> ashenEnemies = new ArrayList<>();
    private final ArrayList<BulletKin> bulletKins = new ArrayList<>();
    private final ArrayList<TreasureBox> treasureBoxes = new ArrayList<>();
    private final ArrayList<Wall> walls = new ArrayList<>();
    private final ArrayList<River> rivers = new ArrayList<>();
    private final ArrayList<Table> tables = new ArrayList<>();
    private final ArrayList<Basket> baskets = new ArrayList<>();
    private final ArrayList<Fireball> fireballs = new ArrayList<>();
    private Door primaryDoor;
    private Door secondaryDoor;

    private Key roomKey = null;
    private boolean keyCollected = false;
    private static final HashSet<String> collectedKeys = new HashSet<>();

    /**
     * Parses the game properties file to initialize all entities for a specific room,
     * including doors, enemies, and environment objects.
     * @param gameProperties The Properties object containing entity data.
     * @param roomName The name of the room to load entities for.
     * @param room The BattleRoom instance (required for Door initialization).
     */
    public void initEntities(Properties gameProperties, String roomName, BattleRoom room) {
        for (Map.Entry<Object, Object> entry : gameProperties.entrySet()) {
            String roomSuffix = String.format(".%s", roomName);
            if (entry.getKey().toString().contains(roomSuffix)) {
                String objectType = entry.getKey().toString()
                        .substring(0, entry.getKey().toString().length() - roomSuffix.length());
                String propertyValue = entry.getValue().toString();
                if (propertyValue.equals("0")) {
                    continue;
                }
                if (objectType.equals("primarydoor") || objectType.equals("secondarydoor")) {
                    String[] coordinates = propertyValue.split(",");
                    if (objectType.equals("primarydoor")) {
                        primaryDoor = new Door(IOUtils.parseCoords(propertyValue), coordinates[2], room);
                    } else {
                        secondaryDoor = new Door(IOUtils.parseCoords(propertyValue), coordinates[2], room);
                    }
                    continue;
                }
                String[] coordsArray = propertyValue.split(";");

                for (String coords : coordsArray) {
                    switch (objectType) {
                        case "keyBulletKin":
                            ArrayList<Point> path = new ArrayList<>();
                            for (String coord : propertyValue.split(";")) {
                                path.add(IOUtils.parseCoords(coord));
                            }
                            KeyBulletKin enemy = new KeyBulletKin(path);
                            enemy.getKey().setActive(false);
                            keyBulletKin.add(enemy);
                            coords = null;
                            break;
                        case "bulletKin":
                            BulletKin bulletKin = new BulletKin(IOUtils.parseCoords(coords));
                            bulletKins.add(bulletKin);
                            break;
                        case "ashenBulletKin":
                            AshenEnemy ashenEnemy = new AshenEnemy(IOUtils.parseCoords(coords));
                            ashenEnemies.add(ashenEnemy);
                            break;
                        case "wall":
                            Wall wall = new Wall(IOUtils.parseCoords(coords));
                            walls.add(wall);
                            break;
                        case "treasurebox":
                            TreasureBox treasureBox = new TreasureBox(IOUtils.parseCoords(coords),
                                    Integer.parseInt(coords.split(",")[2]));
                            treasureBoxes.add(treasureBox);
                            break;
                        case "basket":
                            Basket basket = new Basket(IOUtils.parseCoords(coords));
                            baskets.add(basket);
                            break;
                        case "river":
                            River river = new River(IOUtils.parseCoords(coords));
                            rivers.add(river);
                            break;
                        case "table":
                            Table table = new Table(IOUtils.parseCoords(coords));
                            tables.add(table);
                            break;
                        default:
                            break;
                    }
                    if (coords == null) break;
                }
            }
        }
    }

    /**
     * Updates and draws the primary door.
     *
     * @param currCharacter The current character.
     */
    public void updatePrimaryDoor(Character currCharacter) {
        if (primaryDoor != null) {
            primaryDoor.update(currCharacter);
            primaryDoor.draw();
        }
    }

    /**
     * Updates and draws the secondary door.
     *
     * @param currCharacter The current character.
     */
    public void updateSecondaryDoor(Character currCharacter) {
        if (secondaryDoor != null) {
            secondaryDoor.update(currCharacter);
            secondaryDoor.draw();
        }
    }

    /**
     * Draws both primary and secondary doors.
     */
    public void drawDoors() {
        if (primaryDoor != null) primaryDoor.draw();
        if (secondaryDoor != null) secondaryDoor.draw();
    }

    /**
     * Finds a door by its destination room name.
     *
     * @param roomName The target room name.
     * @return The corresponding Door object.
     */
    public Door findDoorByDestination(String roomName) {
        if (primaryDoor.toRoomName.equals(roomName)) return primaryDoor;
        return secondaryDoor;
    }

    /**
     * Unlocks both doors
     */
    public void unlockAllDoors() {
        if (primaryDoor != null) primaryDoor.unlock(false);
        if (secondaryDoor != null) secondaryDoor.unlock(false);
    }

    /**
     * Returns a list of collidable environment objects in the room.
     *
     * @return List of collidable objects.
     */
    public List<Object> getCollidableEnvironment() {
        List<Object> environment = new ArrayList<>();
        environment.addAll(walls);
        environment.addAll(rivers);
        environment.addAll(tables);
        environment.addAll(baskets);
        if (primaryDoor != null && !primaryDoor.isUnlocked()) environment.add(primaryDoor);
        if (secondaryDoor != null && !secondaryDoor.isUnlocked()) environment.add(secondaryDoor);

        return environment;
    }

    /**
     * Updates and draws all environment objects and handles key collection.
     *
     * @param input        User input.
     * @param currCharacter The current character.
     * @param roomName     Current room name.
     */
    public void updateAndDrawEnvironment(Input input, Character currCharacter, String roomName) {
        // Update and draw environment objects
        for (Wall wall : walls) { wall.update(currCharacter); wall.draw(); }
        for (River river : rivers) { river.update(currCharacter); river.draw(); }
        for (Table table : tables) { if (!table.isDestroyed()) { table.update(currCharacter); table.draw(); } }
        for (Basket bk : baskets) { if (!bk.isDestroyed()) { bk.update(currCharacter); bk.draw(); } }
        for (TreasureBox treasureBox : treasureBoxes) {
            if (!treasureBox.isDestroyed()) {
                treasureBox.update(input, currCharacter);
                treasureBox.draw();
            }
        }
        if (!collectedKeys.contains(roomName)) {
            for (KeyBulletKin enemy : keyBulletKin) {
                if (enemy.isDead() && enemy.getKey().getActive()) {
                    roomKey = enemy.getKey();
                    break;
                }
            }
        } else {
            roomKey = null;
        }
        if (roomKey != null && roomKey.getActive()) {
            roomKey.draw();
            if (currCharacter.getCurrImage().getBoundingBoxAt(currCharacter.getPosition()).intersects(roomKey.getBoundingBox())) {
                currCharacter.pickUpKey(roomKey);
                roomKey.setActive(false);
                keyCollected = true;
                collectedKeys.add(roomName);
            }
        }
    }

    /**
     * Updates enemy behavior and returns newly spawned fireballs.
     *
     * @param currCharacter The current character.
     * @return List of new fireballs.
     */
    public ArrayList<Fireball> updateAndShootEnemies(Character currCharacter) {
        ArrayList<Fireball> newFireballs = new ArrayList<>();
        for (AshenEnemy ashenEnemy : ashenEnemies) {
            if (ashenEnemy.isActive()) {
                newFireballs.addAll(ashenEnemy.updateAndShoot(currCharacter));
                ashenEnemy.update(currCharacter);
                ashenEnemy.draw();
            }
        }
        for (BulletKin bulletKin : bulletKins) {
            if (bulletKin.isActive() && !bulletKin.isDead()) {
                newFireballs.addAll(bulletKin.updateAndShoot(currCharacter));
                bulletKin.update(currCharacter);
                bulletKin.draw();
            }
        }
        for (KeyBulletKin enemy : keyBulletKin) {
            if (enemy.isActive()) {
                enemy.update(currCharacter);
                enemy.draw();
            }
        }
        return newFireballs;
    }

    /**
     * Resets room-specific states such as keys and enemy activity.
     */
    public void resetRoomState() {
        if (roomKey != null) {
            roomKey.setActive(false);
            roomKey = null;
        }
        setKeyCollected(false);
        for (KeyBulletKin enemy : keyBulletKin) {
            enemy.getKey().setActive(false);
            enemy.setActive(false);
        }
    }

    /**
     * Draws all entities, including environment and active enemies.
     *
     * @param currCharacter The current character.
     */
    public void drawAll(Character currCharacter) {
        for (Wall wall : walls) wall.draw();
        for (River river : rivers) river.draw();
        for (Table table : tables) if (!table.isDestroyed()) table.draw();
        for (Basket bk : baskets) if (!bk.isDestroyed()) bk.draw();
        for (TreasureBox treasureBox : treasureBoxes) {if (!treasureBox.isDestroyed()) treasureBox.draw();}
        for (KeyBulletKin enemy : keyBulletKin) {if (enemy.isActive()) enemy.draw();}
        for (AshenEnemy ashenEnemy : ashenEnemies) {if (ashenEnemy.isActive()) ashenEnemy.draw();}
        for (BulletKin bulletKin : bulletKins) {if (bulletKin.isActive() && !bulletKin.isDead()) bulletKin.draw();}
        for (Fireball f : fireballs) {if (f.isActive()) f.draw();}
        if (currCharacter != null) {for (Bullet b : currCharacter.getBullets()) {if (b.isActive()) b.draw();}}
        if (roomKey != null && roomKey.getActive()) {
            roomKey.draw();
        }
    }

    /**
     * Checks if there are no more active enemies in the room.
     *
     * @return True if all enemies are defeated.
     */
    public boolean noMoreEnemies() {
        for (KeyBulletKin enemy : keyBulletKin) {if (enemy.isActive()) return false;}
        for (BulletKin bk : bulletKins) {if (!bk.isDead()) return false;}
        for (AshenEnemy enemy : ashenEnemies) {if (!enemy.isDead()) return false;}
        return true;
    }

    /**
     * Activates all enemies in the room.
     */
    public void activateEnemies() {
//        KeyBulletKin.resetHealth();
        for (KeyBulletKin enemy : keyBulletKin) { enemy.setActive(true); }
        for (BulletKin enemy : bulletKins) { enemy.setActive(true); }
        for (AshenEnemy enemy: ashenEnemies) { enemy.setActive(true);}
    }

    /**
     * Gets the list of KeyBulletKin enemies.
     *
     * @return List of KeyBulletKin.
     */
    public ArrayList<KeyBulletKin> getKeyBulletKin() { return keyBulletKin; }

    /**
     * Gets the list of AshenEnemies.
     *
     * @return List of AshenEnemy.
     */
    public ArrayList<AshenEnemy> getAshenEnemies() { return ashenEnemies; }

    /**
     * Gets the list of BulletKin enemies.
     *
     * @return List of BulletKin.
     */
    public ArrayList<BulletKin> getBulletKins() { return bulletKins; }

    /**
     * Gets the list of fireballs in the current room.
     *
     * @return List of Fireball.
     */
    public ArrayList<Fireball> getFireballs() { return fireballs; }

    /**
     * Sets the keyCollected flag for the current room.
     *
     * @param keyCollected True if the key was collected.
     */
    public void setKeyCollected(boolean keyCollected) { this.keyCollected = keyCollected; }
}