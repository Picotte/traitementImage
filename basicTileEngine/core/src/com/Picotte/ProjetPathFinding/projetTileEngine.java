package com.Picotte.ProjetPathFinding;

import models.camera;
import models.pathFinderAstar;
import models.pathFinder;
import models.tile;
import models.tileMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class projetTileEngine extends ApplicationAdapter implements InputProcessor{
    SpriteBatch batch;
    tileMap map = new tileMap();

    private pathFinder finder;
    private models.path path;

    public boolean modeDemo = false;

    public boolean trouver = false;
    public boolean passer = false;
    public int canPath = 0;

    int tileSize = 32;

    int carreLargeur;
    int carreHauteur;

    float delay = 0;


    private int selectedx = -1;
    private int selectedy = -1;

    private int lastFindX = -1;
    private int lastFindY = -1;

    private BitmapFont font;

    @Override
    public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont();
        tile.setTileSetTexture (new Texture("projetPathFinding.png"));

        generate();

    }

    private void generate() {
        carreLargeur = Gdx.graphics.getWidth() / tileSize + 1;
        carreHauteur = Gdx.graphics.getHeight() / tileSize + 2;
        trouver = false;
        passer = false;

        lastFindX = (int) Math.ceil(Math.random() * 23);
        lastFindY = (int) Math.ceil(Math.random() * 17);
        selectedx = (int) Math.ceil(Math.random() * 23);
        selectedy = (int) Math.ceil(Math.random() * 17);

        int test;
        do {
            test = 0;
            while (lastFindY > 2 && lastFindY < 12 && lastFindX == 7 && lastFindY != lastFindX) {
                lastFindY = (int) Math.ceil(Math.random() * 17);
                lastFindX = (int) Math.ceil(Math.random() * 23);
                test++;
            }
            while (lastFindY > 7 && lastFindY < 15 && lastFindX == 17) {
                lastFindY = (int) Math.ceil(Math.random() * 17);
                lastFindX = (int) Math.ceil(Math.random() * 23);
                test++;
            }
            while (lastFindY > 7 && lastFindY < 15 && lastFindX == 22) {
                lastFindY = (int) Math.ceil(Math.random() * 17);
                lastFindX = (int) Math.ceil(Math.random() * 23);
                test++;
            }
            while (lastFindX > 2 && lastFindX < 12 && lastFindY == 12 && lastFindX != lastFindY) {
                lastFindX = (int) Math.ceil(Math.random() * 23);
                lastFindY = (int) Math.ceil(Math.random() * 17);
                test++;
            }
            while (lastFindX > 16 && lastFindX < 23 && lastFindY == 7) {
                lastFindX = (int) Math.ceil(Math.random() * 23);
                lastFindY = (int) Math.ceil(Math.random() * 17);
                test++;
            }
            while(lastFindX == selectedx && lastFindY == selectedy) {
                lastFindX = (int) Math.ceil(Math.random() * 23);
                lastFindY = (int) Math.ceil(Math.random() * 17);
                test++;
            }
        } while (test > 0);


        map.generateTest(lastFindX, lastFindY,selectedx,lastFindY);

        finder = new pathFinderAstar(map, 500);


        Gdx.input.setInputProcessor(this);
        Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render ()
    {
        if(modeDemo)
        {
            if(delay < 5)
            {
                update();
                draw();
            }
            else
            {
                generate();
                draw();
                delay = 0;
            }
            delay += Gdx.graphics.getDeltaTime();
        }
        else
        {
            update();
            draw();
        }

    }

    private void update()
    {
        if(modeDemo == false)
        {
            if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
                Gdx.app.exit();
            }
            if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
                generate();
                draw();
            }
        }
    }

    private void draw()
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        Vector2 firstSquare = new Vector2(camera.getLocation().x / (float) tileSize, camera.getLocation().y / (float) tileSize);
        int firstX = (int) firstSquare.x;
        int firstY = (int) firstSquare.y;


        Vector2 squareOffset = new Vector2(camera.getLocation().x % tileSize, camera.getLocation().y % tileSize);
        int offsetX = (int) squareOffset.x;
        int offsetY = (int) squareOffset.y;


        batch.begin();

        Rectangle srcRect = null;
        for (int y = 0; y < carreHauteur; y++)
        {
            int positionY = (y * tileSize) - offsetY;

            for (int x = 0; x < carreLargeur; x++)
            {
                srcRect = tile.getSourceRectangle(map.getRow(y + firstY).getCell(x + firstX).getTileID());

                batch.draw(tile.getTileSetTexture(),
                        (x * tileSize) - offsetX, positionY,
                        (int) srcRect.x, (int) srcRect.y,
                        (int) srcRect.width, (int) srcRect.height);

                if (map.getRow(y + firstY).getCell(x + firstX).getTileID() == 4)
                {
                    this.selectedx = x;
                    this.selectedy = y;
                    canPath+=1;
                }
                if (map.getRow(y + firstY).getCell(x + firstX).getTileID() == 5)
                {
                    this.lastFindX = x + firstX;
                    this.lastFindY = y + firstY;
                    canPath+=1;
                }
            }
        }
        font.setColor(new Color(0,0,0,255));
        font.draw(batch, "ALEXANDRE PICOTTE  ---------  ANNÉE 2016", 20, 20);
        batch.end();
        if(canPath == 2)
        {
            if (this.trouver == false)
            {
                path = finder.findPath(selectedx, selectedy, lastFindX, lastFindY);
                trouver = true;
            }
        }
        if(path != null && trouver == true && passer == false)
        {
            batch.begin();
            for (int x=0;x<carreLargeur;x++)
            {
                for (int y=0;y<carreHauteur;y++)
                {
                    int positionY = (y * tileSize) - offsetY;
                    if (path != null)
                    {
                        srcRect = tile.getSourceRectangle(1);
                        if (path.contains(x, y))
                        {
                            if(x == lastFindX && y == lastFindY)
                            {
                                srcRect = tile.getSourceRectangle(4);
                            }
                            else if(x == selectedx && y == selectedy)
                            {
                                srcRect = tile.getSourceRectangle(5);
                            }
                            else
                            {
                                srcRect = tile.getSourceRectangle(1);
                            }
                            canPath = 0;
                            batch.draw(tile.getTileSetTexture(),
                                    (x * tileSize) - offsetX, positionY,
                                    (int) srcRect.x, (int) srcRect.y,
                                    (int) srcRect.width, (int) srcRect.height);
                        }
                    }

                }
            }
            batch.end();
        }

        //System.out.println(this.path.contains(x,y));

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!modeDemo) {
            //le click gauche est pour la case target, donc l'endroit que l'on veux atteindre (la tete du trooper)
            if (button == Input.Buttons.LEFT)
            {
                trouver = false;
                passer = false;
                this.lastFindX = (screenX / tileSize);
                this.lastFindY = (screenY / tileSize);
                lastFindY = reversePositionLastY(this.lastFindY);
                if(lastFindY > 2 && lastFindY < 12 && lastFindX == 7 && lastFindY != lastFindX) {
                    lastFindY = 2;
                    System.out.print("aaa");
                    System.out.print(selectedx);
                }
                if(lastFindY > 7 && lastFindY < 15 && lastFindX == 17) {
                    lastFindY = 15;
                    lastFindX = 17;
                }
                if(lastFindY > 7 && lastFindY < 15 && lastFindX == 22) {
                    lastFindY = 15;
                    lastFindX = 22;
                }
                if(lastFindX > 2 && lastFindX < 12 && lastFindY == 12 && lastFindX != lastFindY) {
                    lastFindX = 2;
                }
                if(lastFindX > 16 && lastFindX < 23 && lastFindY == 7) {
                    lastFindX = 16;
                }

                if(lastFindX == 0)
                {
                    lastFindX = 1;
                }
                if (lastFindY == 0)
                {
                    lastFindY = 17;
                }
                if(lastFindX == 24)
                {
                    lastFindX = 23;
                }
                if (lastFindY == 18)
                {
                    lastFindY = 1;
                }

                if(lastFindX == selectedx && lastFindY == selectedy &&  selectedy == 2 && selectedx == 7)
                {
                    lastFindY--;
                }
                else if(lastFindX == selectedx && lastFindY == selectedy && selectedy == 1)
                {
                    lastFindY--;
                }
                else if(lastFindX == selectedx && lastFindY == selectedy && selectedy != 1)
                {
                    lastFindY++;
                    System.out.print("sss");
                }

                map.generateTest(lastFindX, lastFindY, selectedx, selectedy);
                finder = new pathFinderAstar(map, 500);
                draw();

            }
            //le click droit est la case ou nous débuttons notre pathfinding, donc la case noir avec le carre rouge
            if (button == Input.Buttons.RIGHT) {
                trouver = false;
                passer = false;
                this.selectedx = (screenX / tileSize);
                this.selectedy = (screenY / tileSize);
                this.selectedy = reversePositionSelectedY(this.selectedy);

                if(selectedy > 2 && selectedy < 12 && selectedx == 7 && selectedy != selectedx) {
                    selectedy = 2;
                }
                if(selectedy > 7 && selectedy < 15 && selectedx == 17) {
                    selectedy = 15;
                    selectedx = 17;
                }
                if(selectedy > 7 && selectedy < 15 && selectedx == 22) {
                    selectedy = 15;
                    selectedx = 22;
                }
                if(selectedx > 2 && selectedx < 12 && selectedy == 12 && selectedx != selectedy) {
                    selectedx = 2;
                }
                if(selectedx > 16 && selectedx < 23 && selectedy == 7) {
                    selectedx = 16;
                }

                if(selectedx == 0)
                {
                    selectedx = 1;
                }
                if (selectedy == 0)
                {
                    selectedy = 17;
                }
                if(selectedx == 24)
                {
                    selectedx = 23;
                }
                if (selectedy == 18)
                {
                    selectedy = 1;
                }
                if(selectedx == lastFindX && selectedy == lastFindY && lastFindY != 1)
                {
                    selectedy--;
                }
                else if(selectedx == lastFindX && selectedy == lastFindY && lastFindY == 1)
                {
                    selectedy++;
                }

                map.generateTest(lastFindX, lastFindY, selectedx, selectedy);
                finder = new pathFinderAstar(map, 500);
                draw();

            }
        }
        return false;
    }

    private int reversePositionLastY(int lastFindY)
    {
        if(lastFindY == 1)
        {
            this.lastFindY = 17;
        }
        if(lastFindY == 2)
        {
            this.lastFindY = 16;
        }
        if(lastFindY == 3)
        {
            this.lastFindY = 15;
        }
        if(lastFindY == 4)
        {
            this.lastFindY = 14;
        }
        if(lastFindY == 5)
        {
            this.lastFindY = 13;
        }
        if(lastFindY == 6)
        {
            this.lastFindY = 12;
        }
        if(lastFindY == 7)
        {
            this.lastFindY = 11;
        }
        if(lastFindY == 8)
        {
            this.lastFindY = 10;
        }
        if(lastFindY == 10)
        {
            this.lastFindY = 8;
        }
        if(lastFindY == 11)
        {
            this.lastFindY = 7;
        }
        if(lastFindY == 12)
        {
            this.lastFindY = 6;
        }
        if(lastFindY == 13)
        {
            this.lastFindY = 5;
        }
        if(lastFindY == 14)
        {
            this.lastFindY = 4;
        }
        if(lastFindY == 15)
        {
            this.lastFindY = 3;
        }
        if(lastFindY == 16)
        {
            this.lastFindY = 2;
        }
        if(lastFindY == 17)
        {
            this.lastFindY = 1;

        }

        return this.lastFindY;
    }
    private int reversePositionSelectedY(int selectedy)
    {
        if(selectedy == 1)
        {
            this.selectedy = 17;
        }
        if(lastFindY == 2)
        {
            this.selectedy = 16;
        }
        if(selectedy == 3)
        {
            this.selectedy = 15;
        }
        if(selectedy == 4)
        {
            this.selectedy = 14;
        }
        if(selectedy == 5)
        {
            this.selectedy = 13;
        }
        if(selectedy == 6)
        {
            this.selectedy = 12;
        }
        if(selectedy == 7)
        {
            this.selectedy = 11;
        }
        if(selectedy == 8)
        {
            this.selectedy = 10;
        }
        if(selectedy == 10)
        {
            this.selectedy = 8;
        }
        if(selectedy == 11)
        {
            this.selectedy = 7;
        }
        if(selectedy == 12)
        {
            this.selectedy = 6;
        }
        if(selectedy == 13)
        {
            this.selectedy = 5;
        }
        if(selectedy == 14)
        {
            this.selectedy = 4;
        }
        if(selectedy == 15)
        {
            this.selectedy = 3;
        }
        if(selectedy == 16)
        {
            this.selectedy = 2;
        }
        if(selectedy == 17)
        {
            this.selectedy = 1;

        }

        return this.selectedy;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}