package com.mygdx.game.screens;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Screen;
        import com.badlogic.gdx.audio.Music;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.Animation;
        import com.badlogic.gdx.graphics.g2d.BitmapFont;
        import com.badlogic.gdx.graphics.g2d.GlyphLayout;
        import com.badlogic.gdx.graphics.g2d.TextureRegion;
        import com.badlogic.gdx.math.Vector2;
        import com.badlogic.gdx.physics.box2d.World;
        import com.mygdx.game.Alex241Intro;
        import com.mygdx.game.tools.GridPosition;

        import java.util.concurrent.ThreadLocalRandom;

public class CreditScreen implements Screen {
    TextureRegion tex1;
    TextureRegion tex2;
    TextureRegion tex3;
    TextureRegion tex4;
    TextureRegion tex5;
    TextureRegion tex6;
    TextureRegion tex7;
    TextureRegion tex8;
    Alex241Intro game;
    BitmapFont scoreFont;
    BitmapFont specialFont;
    World world;
    Animation<TextureRegion> animation;
    float elapsed;
    GlyphLayout[] allText = new GlyphLayout[19];
    GridPosition[] positions = new GridPosition[19];
    private Music theme;
    private String dirSlash;

    public CreditScreen(Alex241Intro game, String dirSlash) {
        this.game = game;
        this.dirSlash = dirSlash;
        world = new World(new Vector2(0f,-9.81f), true);
        scoreFont = new BitmapFont(Gdx.files.internal("fonts" + dirSlash + "score.fnt"));
        specialFont = new BitmapFont(Gdx.files.internal("fonts" + dirSlash + "score2.fnt"));
        tex1 = new TextureRegion(new Texture("frames" + dirSlash + "0.gif"));
        tex2 = new TextureRegion(new Texture("frames" + dirSlash + "1.gif"));
        tex3 = new TextureRegion(new Texture("frames" + dirSlash + "2.gif"));
        tex4 = new TextureRegion(new Texture("frames" + dirSlash + "3.gif"));
        tex5 = new TextureRegion(new Texture("frames" + dirSlash + "4.gif"));
        tex6 = new TextureRegion(new Texture("frames" + dirSlash + "5.gif"));
        tex7 = new TextureRegion(new Texture("frames" + dirSlash + "6.gif"));
        tex8 = new TextureRegion(new Texture("frames" + dirSlash + "7.gif"));
        animation = new Animation<TextureRegion>(0.1f, tex1, tex2, tex3, tex4, tex5, tex6, tex7, tex8);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        this.theme = Gdx.audio.newMusic(Gdx.files.internal("credits.mp3"));


        allText[0] = new GlyphLayout(specialFont, "THANK YOU");
        allText[1] = new GlyphLayout(specialFont, "FOR PLAYING!");
        allText[2] = new GlyphLayout(specialFont, "Developed by Kyle Zeller");
        allText[3] = new GlyphLayout(specialFont, "Tribute to Professor Aleksandar Pantaleev");
        allText[4] = new GlyphLayout(specialFont, "A Special Thanks to:");
        allText[5] = new GlyphLayout(specialFont, "The Audio Artists:");
        allText[6] = new GlyphLayout(specialFont, "Mrthenoronha");
        allText[7] = new GlyphLayout(specialFont, "joshuaempyre");
        allText[8] = new GlyphLayout(specialFont, "The Artists:");
        allText[9] = new GlyphLayout(specialFont, "Brandon J. Caruso");
        allText[10] = new GlyphLayout(specialFont, "Traivs Cook");
        allText[11] = new GlyphLayout(specialFont, "Hunter Donley");
        allText[12] = new GlyphLayout(specialFont, "Joel Gleason");
        allText[13] = new GlyphLayout(specialFont, "Brooke Pagliuso");
        allText[14] = new GlyphLayout(specialFont, "Julie Zeller");
        allText[15] = new GlyphLayout(specialFont, "UniverseGod");
        allText[16] = new GlyphLayout(specialFont, "The Voice Over Artist:");
        allText[17] = new GlyphLayout(specialFont, "David Hite");
        allText[18] = new GlyphLayout(specialFont, "Website: http://cs.oswego.edu/~kzeller/");


        positions[0] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[0].width/2, (int)-allText[0].height);
        positions[1] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[1].width/2, (int)(-allText[0].height)*5);
        positions[2] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[2].width/2, (int)(-allText[0].height)*11);
        positions[3] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[3].width/2, (int)(-allText[0].height)*17);
        positions[4] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[4].width/2, (int)(-allText[0].height)*23);
        positions[5] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[5].width/2, (int)(-allText[0].height)*28);
        positions[6] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[6].width/2, (int)(-allText[0].height)*31);
        positions[7] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[7].width/2, (int)(-allText[0].height)*34);
        positions[8] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[8].width/2, (int)(-allText[0].height)*39);
        positions[9] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[9].width/2, (int)(-allText[0].height)*42);
        positions[10] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[10].width/2, (int)(-allText[0].height)*45);
        positions[11] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[11].width/2, (int)(-allText[0].height)*48);
        positions[12] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[12].width/2, (int)(-allText[0].height)*51);
        positions[13] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[13].width/2, (int)(-allText[0].height)*54);
        positions[14] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[14].width/2, (int)(-allText[0].height)*57);
        positions[15] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[15].width/2, (int)(-allText[0].height)*60);
        positions[16] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[16].width/2, (int)(-allText[0].height)*65);
        positions[17] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[17].width/2, (int)(-allText[0].height)*68);
        positions[18] = new GridPosition(Gdx.graphics.getWidth()/2 - (int)allText[18].width/2, (int)(-allText[0].height)*73);
    }

    @Override
    public void show() {
        if (!theme.isPlaying()) {
            theme.play();
            theme.setLooping(true);
        }
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(animation.getKeyFrame(elapsed), 0f, 0f);

        //draw text
        for (int i = 0; i < allText.length; i++) {
            specialFont.draw(game.batch, allText[i], positions[i].getX(), positions[i].getY());
        }

        GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "MAIN MENU");

        float mainMenuX = Gdx.graphics.getWidth() - mainMenuLayout.width - mainMenuLayout.height;
        float mainMenuY = Gdx.graphics.getHeight() - mainMenuLayout.height - mainMenuLayout.height;

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        //if main menu
        if (Gdx.input.justTouched()) {
            if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainMenuScreen(game, dirSlash));
                return;
            }
        }

        scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);

        game.batch.end();

        //update method
        this.update();
    }

    private void update() {
        if (positions[18].getY() > Gdx.graphics.getHeight() + allText[18].height) {
            this.dispose();
            game.setScreen(new MainMenuScreen(game, dirSlash));
        } else {
            for (int i = 0; i < positions.length; i++) {
                positions[i].setY(positions[i].getY() + 1);
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (theme.isPlaying()) {
            theme.stop();
            theme.setLooping(false);
        }
    }
}
