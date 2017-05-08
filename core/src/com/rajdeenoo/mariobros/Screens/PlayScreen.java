package com.rajdeenoo.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rajdeenoo.mariobros.MarioBros;
import com.rajdeenoo.mariobros.Scenes.Hud;
import com.rajdeenoo.mariobros.Sprites.Goomba;
import com.rajdeenoo.mariobros.Sprites.Mario;
import com.rajdeenoo.mariobros.Tools.B2WorldCreator;
import com.rajdeenoo.mariobros.Tools.WorldContactListener;

/**
 * Created by raj on 01/05/2017.
 */

public class PlayScreen implements Screen{

    private MarioBros game;
    private TextureAtlas atlas;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;


    // Tiles map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box 2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    // Sprites
    private Mario player;

    private Goomba goomba;


    private Music music;


    public PlayScreen(MarioBros game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;

        // create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        // create a FitViewPort to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM,MarioBros.V_HEIGHT / MarioBros.PPM,gamecam);

        // create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        // load our map  and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);

        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set(gamePort.getWorldWidth() / 2 , gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0,-10), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);

        //create mario in our game world
        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        goomba = new Goomba(this, .64f, .32f);

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }


    @Override
    public void show() {

    }


    public void handleInput(float dt) {
        //
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(new Vector2(0,4f),player.b2body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f,0), player.b2body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0), player.b2body.getWorldCenter(), true);
    }

    public void update(float dt) {
        // handle user input first
        handleInput(dt);

        world.step(1/60f,6,2);

        player.update(dt);
        goomba.update(dt);
        hud.update(dt);

        // attach our gamecam to our players.x coordinates
        gamecam.position.x = player.b2body.getPosition().x;

        // update our gamecam with correct coordinates after changes
        gamecam.update();
        // tell our renderer to draw only what our camera can see in our game world
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        // separate our update logic from render
        update(delta);

        // Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render our game map
        renderer.render();

        // renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        goomba.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
