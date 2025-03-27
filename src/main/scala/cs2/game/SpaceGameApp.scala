package cs2.game

import scalafx.application.JFXApp
import scalafx.Includes._
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.Color
import scalafx.event.Event
import scalafx.scene.input.MouseEvent
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import scalafx.scene.effect.Effect
import scalafx.animation.AnimationTimer
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.text.Font
import scalafx.scene.text.Text
import scalafx.geometry.Pos
import scalafx.geometry.Pos.TopCenter
import scalafx.scene.text.TextAlignment
import scalafx.scene.canvas.GraphicsContext
import cs2.util.Vec2
import javax.net.ssl.TrustManager
import java.awt.RenderingHints.Key
import scala.collection.mutable.Buffer
import scala.collection.mutable.Set
import scala.collection.mutable.Stack


object SpaceGameApp extends JFXApp {



var gs = new GameState()


var gsStack = new Stack[GameState]()


  var KeysTrackedSet = Set[KeyCode]()
  var BulletRemoveBuffer = Buffer[Bullet]()

  stage = new JFXApp.PrimaryStage {
    title = "Space Invaders Over Saturn"
    scene = new Scene(1280, 800) {
      val canvas = new Canvas(1280, 800)
      content = canvas
      val g = canvas.graphicsContext2D

      var showStartScreen = true
      var beginGame = false
      var showPauseScreen = false
      var showDeadScreen = false
      var showWaveClearScreen = false
      var showWinScreen = false

    
      canvas.requestFocus()

      canvas.onKeyPressed = (e: KeyEvent) => {
        if (e.code == KeyCode.Enter) {
          beginGame = true
          gs.ScoreDisplay = true
          gs.LivesDisplay = true
          gs.TimeDisplay = true
          showWinScreen = false
          showPauseScreen = false
          showDeadScreen = false
          showStartScreen = false
          showWaveClearScreen = false
        }

        if (e.code == KeyCode.N) {
          beginGame = true
          gs.ScoreDisplay = true
          gs.LivesDisplay = true
          gs.TimeDisplay = true
          gs.Swarm = new EnemySwarm(6, 3)
          showDeadScreen = false
          showStartScreen = false
          showPauseScreen = false
          showWaveClearScreen = false
          showWinScreen = false
          gs.Time = 100
          gs.Lives = 5
          gs.WaveCount = 0
          gs.killcounter = 0
          gs.bulletinterception = 0
          rewindcounter = 0
        }
        
        if (e.code == KeyCode.Escape) {
          showPauseScreen = true
          beginGame = false
          showDeadScreen = false
          showStartScreen = false
          showWaveClearScreen = false
          showWinScreen = false
          gs.LivesDisplay = false
          gs.ScoreDisplay = false
          gs.TimeDisplay = false
        }

        

        if (e.code == KeyCode.Left || e.code == KeyCode.A) {
          KeysTrackedSet += KeyCode.Left
          // Gamer.moveLeft()
        }

        if (e.code == KeyCode.Right || e.code == KeyCode.D) {
          KeysTrackedSet += KeyCode.Right
          // Gamer.moveRight()
        }
        if (e.code == KeyCode.Up || e.code == KeyCode.W) {
          KeysTrackedSet += KeyCode.Up
          // Gamer.moveUp()
        }
        if (e.code == KeyCode.Down || e.code == KeyCode.S) {
          KeysTrackedSet += KeyCode.Down
          // Gamer.moveDown()
        }

        if (e.code == KeyCode.Space) {
          KeysTrackedSet += KeyCode.Space
          // BulletBuffer += Gamer.shoot()
        }


        if (e.code == KeyCode.R){
          KeysTrackedSet += KeyCode.R
        }

        if (e.code == KeyCode.M){
          KeysTrackedSet += KeyCode.M
        }

        /*if (e.code == KeyCode.T) {
          Gamer.moveTo(Vec2(615, 720))
        }*/ //Teleportation powerup?
        /*
                if (e.code == KeyCode.W){
                    KeysTrackedSet += KeyCode.W
                    Gamer.moveUp()
                }
                if (e.code == KeyCode.A) {
                    KeysTrackedSet += KeyCode.A
                    Gamer.moveLeft()
                }
                if (e.code == KeyCode.S) {
                    KeysTrackedSet += KeyCode.S
                    Gamer.moveDown()
                }
                if (e.code == KeyCode.D) {
                    KeysTrackedSet += KeyCode.D
                    Gamer.moveRight()
                }
         */

      }
      canvas.onKeyReleased = (e: KeyEvent) => {
        if (e.code == KeyCode.Left || e.code == KeyCode.A) {
          KeysTrackedSet -= KeyCode.Left
        }
        if (e.code == KeyCode.Right || e.code == KeyCode.D) {
          KeysTrackedSet -= KeyCode.Right
        }
        if (e.code == KeyCode.Up || e.code == KeyCode.W) {
          KeysTrackedSet -= KeyCode.Up
        }
        if (e.code == KeyCode.Down || e.code == KeyCode.S) {
          KeysTrackedSet -= KeyCode.Down
        }
        if (e.code == KeyCode.Space) {
          KeysTrackedSet -= KeyCode.Space
        }

        if (e.code == KeyCode.R){
          KeysTrackedSet -= KeyCode.R
        }

        if (e.code == KeyCode.K){
          KeysTrackedSet -= KeyCode.M
        }
      }


      var enemyslowdown = 0
      var machinegun = -15
      var slowdown = machinegun
      var rewindcounter:Double = 0
      val timer = AnimationTimer(t => {

        if (showStartScreen == true && showPauseScreen == false) {
          g.setTextAlign(TextAlignment.Center);g.setFill(CustomColors.background);g.fillRect(0, 0, canvas.width.value, canvas.height.value);g.drawImage(SpriteList.Saturn, 0, 0); g.setStroke(Color.GhostWhite);g.setFont(FontsLoaded.GemunuLibreEB);g.strokeText("Space Invaders Over Saturn", canvas.width.value / 2, canvas.height.value - 600, 600);g.fillText("Space Invaders Over Saturn", canvas.width.value / 2, canvas.height.value - 600, 600); g.setFont(FontsLoaded.GemunuLibreMed);g.setFill(Color.LightYellow);g.fillText("Press Enter Key to Play", canvas.width.value / 2, canvas.height.value - 200, 300) 

          //copied command of TitleScreen
        }
        if (showPauseScreen == true) {
            g.setFont(FontsLoaded.GemunuLibreReg); g.setFill(CustomColors.pausescreenbg); g.fillRect(0, 0, canvas.width.value, canvas.height.value);g.strokeText("Quit Game?", canvas.width.value / 2, canvas.height.value - 550,400);g.setFont(FontsLoaded.GemunuLibreMed);g.strokeText("Press Enter to Continue or N Key to Start a New Game", canvas.width.value / 2, canvas.height.value - 250, 500)
        }


        else if(!KeysTrackedSet.contains(KeyCode.R) && !showStartScreen){
        
          

        if (beginGame) {
    /*Background*/  g.drawImage(SpriteList.Saturn, 0, 0)
    /*Score*/      g.setFont(FontsLoaded.GemunuLibreLight);g.setFill(Color.GhostWhite);g.fillText("Score: "+gs.Score.toString(), canvas.width.value/3-330, canvas.height.value -750, 300)
    /*Hearts*/     g.drawImage(SpriteList.Heart, 2*canvas.width.value/3 - 500, canvas.height.value - 785);g.setFont(FontsLoaded.GemunuLibreLight);g.setFill(Color.GhostWhite);g.fillText(gs.Lives.toString(), 2*canvas.width.value/3-420, canvas.height.value -750, 300)
    /*WavesCleared*/g.fillText("Waves Cleared: " + gs.WaveCount.toString(), 2*canvas.width.value/3-270, canvas.height.value - 750, 200)
    /*Time*/       g.setFont(FontsLoaded.GemunuLibreLight);g.setFill(Color.GhostWhite);g.fillText("Time: "+gs.Time.toString(), canvas.width.value/2 -375, canvas.height.value - 750, 120)
/*PowerBar*/   g.setFont(FontsLoaded.GemunuLibreExLi);g.setFill(Color.GhostWhite);g.fillText("Rewind Power Remaining: ", 3*canvas.width.value/4 - 100, canvas.height.value-750, 275);g.setFill(Color.DarkRed);g.fillRect(78*canvas.width.value/100, canvas.height.value -780, (250*(rewindcounter/6000.0)), canvas.height.value-763);g.setStroke(Color.SkyBlue);g.strokeRect(78*canvas.width.value/100, canvas.height.value -780, (250*(rewindcounter/6000.0)), canvas.height.value-763)

        gs.Gamer.display(g)
        gs.Swarm.display(g)
        gs.Swarm.swarmMove(gs.calculateEnemySpeedMultiplier());
        gs.count+=1
        enemyslowdown += gs.calculateShootingSpeedFactor().toInt;

        if(rewindcounter >= 0){
        if(rewindcounter <= 5998){
          rewindcounter+=1
        }

        
        if(rewindcounter == 5999){
          rewindcounter = 5999
        }
      }
          
          
            /*if(escape >= 1){
                    g.setFill(CustomColors.pausescreenbg);g.fillRect(0,0, canvas.width.value, canvas.height.value);g.strokeText("Quit Game?", canvas.width.value/2, canvas.height.value-550, 400)*/ // THIS IS PAUSE SCREEN CODE
        if (KeysTrackedSet.contains(KeyCode.Left)) {
          gs.Gamer.moveLeft()
        }
        if (KeysTrackedSet.contains(KeyCode.Right)) {
          gs.Gamer.moveRight()
        }
        if (KeysTrackedSet.contains(KeyCode.Up)) {
          gs.Gamer.moveUp()
        }
        if (KeysTrackedSet.contains(KeyCode.Down)) {
          gs.Gamer.moveDown()
        }

        if (KeysTrackedSet.contains(KeyCode.M)){
          machinegun =  0
        }

        if (!KeysTrackedSet.contains(KeyCode.M)){
          machinegun = -15
        }

        if (KeysTrackedSet.contains(KeyCode.Space)) {
          slowdown += 1
          if (slowdown > 0) {
            slowdown = machinegun
            gs.BulletBuffer += gs.Gamer.shoot()
          }

        } // MACHINE GUN POWER UP LOL but only if without slowdown
        //if (KeysTrackedSet.contains(KeyCode.R)){
          //gs = gsStack.pop()
        //}
        //println(KeysTrackedSet.contains(KeyCode.R))
        }

        

        if (showWaveClearScreen) {
          // Display the "Wave Cleared" screen
          g.setFont(FontsLoaded.GemunuLibreReg)
          // g.setFill(CustomColors.background)
          // g.fillRect(0, 0, canvas.width.value, canvas.height.value)
          g.strokeText("Wave Complete!", canvas.width.value / 2, canvas.height.value - 550, 400)
          g.setFont(FontsLoaded.GemunuLibreMed)
          g.strokeText("Get ready for the next wave!", canvas.width.value / 2, canvas.height.value - 250, 500)
          gs.waveClearTimer +=1
          if (gs.waveClearTimer >= 180) { // Assuming 60 FPS, this is a 3-second delay
            showWaveClearScreen = false
            gs.waveClearTimer = 0
            gs.Swarm = new EnemySwarm(6, 3)
            gs.Swarm.display(g)
          }
        } else {
          gs.Swarm.display(g)
          gs.Swarm.swarmMove(gs.calculateEnemySpeedMultiplier);
        }

         if (gs.Lives == 0 || gs.Time == 0) {
            showPauseScreen = false
            beginGame = false
            showDeadScreen = true
            gs.LivesDisplay = false
            gs.ScoreDisplay = false
            gs.TimeDisplay = false
            showWinScreen = false
            showStartScreen = false
            rewindcounter = 0
        }

        if (showDeadScreen) {
            g.setFill(CustomColors.background);g.fillRect(0,0, canvas.width.value, canvas.height.value);g.drawImage(SpriteList.DeadBackground, 0, 0);g.setFont(FontsLoaded.GemunuLibreEB);g.setFill(Color.Black);g.fillText("You Lose", canvas.width.value/2, 2*canvas.height.value/3);g.setFont(FontsLoaded.GemunuLibreMed);g.fillText("Press N Key to Start a New Game", canvas.width.value / 2, canvas.height.value - 200, 500)
        }   
        if (gs.WaveCount == 5){
          showWinScreen = true
        }
        if (showWinScreen) {
          g.setFill(Color.Black)
          g.fillRect(0, 0, canvas.width.value, canvas.height.value) // Optional: clear background

          // Set font and color for "You Win" message
          g.setFont(FontsLoaded.GemunuLibreLight)
          g.setFill(Color.Gold)
          g.fillText("You Win!", canvas.width.value / 2, canvas.height.value / 2 - 100)  // Adjust text position

          // Show score or other details if needed
          g.setFont(FontsLoaded.GemunuLibreLight)
          g.setFill(Color.White)
          g.fillText(s"Final Score: ${gs.Score}", canvas.width.value / 2, canvas.height.value / 2)

          g.drawImage(SpriteList.Trophy, canvas.width.value / 2 - 160 , canvas.height.value / 2 +80) // Example image
          
          
          g.setFont(FontsLoaded.GemunuLibreLight)
          g.setFill(Color.White)
          g.fillText("Press N to Restart", canvas.width.value / 2, canvas.height.value / 2 + 100)
          beginGame = false
          showPauseScreen = false
          showStartScreen = false
        }

        if(gs.count > 0){
            gs.count = -60
            gs.Time -= 1
        }
        if (enemyslowdown > 0) {
          enemyslowdown = -45
          if (!gs.Swarm.isEmpty) {
            gs.BulletBuffer += gs.Swarm.swarmshoot() 
          }
        }

        for (Bullet <- gs.BulletBuffer) {
          Bullet.display(g)
          Bullet.timeStep()
        }

        for (i <- 0 until gs.BulletBuffer.length) {
          if (
            gs.BulletBuffer(i).initPos.y > 830 || gs.BulletBuffer(i).initPos.y < -30
          )
            BulletRemoveBuffer += gs.BulletBuffer(i)

          val ebp =
            SpriteList.EnemyBullet // enemy bullet pic, need it to specify that only enemy bullets have this action
          if (
            gs.Gamer.intersection(gs.BulletBuffer(i)) && gs.BulletBuffer(i).pic == ebp
          ) {
            gs.Gamer.moveTo(Vec2(615, 720))
            gs.Lives -= 1 
            BulletRemoveBuffer += gs.BulletBuffer(i)
          }
        }
       gs.BulletBuffer --= BulletRemoveBuffer
        for (i <- 0 until gs.BulletBuffer.length) {

          if (gs.Swarm.enemyHit(gs.BulletBuffer(i))) {
            BulletRemoveBuffer += gs.BulletBuffer(i)
            gs.killcounter+=1
           
          }
          if (gs.Swarm.enemyBump(gs.Gamer)) {
            gs.Lives -= 1
            gs.Gamer.moveTo(Vec2(615, 720))
          }

          for (j <- 0 until gs.BulletBuffer.length) {
            if (i != j) {
              if ((gs.BulletBuffer(i).intersection(gs.BulletBuffer(j)))) {
                gs.bulletinterception += 1 
                BulletRemoveBuffer += gs.BulletBuffer(i)
                BulletRemoveBuffer += gs.BulletBuffer(j)
              }
            }

          }

        }
       gs.BulletBuffer --= BulletRemoveBuffer

       gsStack.push(gs.deepcopy())
        // println(rewindcounter)
        if (gs.Swarm.isEmpty() && !showWaveClearScreen) {
          showWaveClearScreen = true;
          gs.waveClearTimer = 0
          gs.WaveCount += 1
        }

         gs.Score = gs.killcounter*100 + gs.bulletinterception*25 //could do += for exponential growth
      }



      else{


      if(gs.Time < 100 && rewindcounter > 0){
        

        if(rewindcounter > 1) {
          rewindcounter -= 1
        }
        if(rewindcounter == 1){
          rewindcounter = 0
        }
        
        gs = gsStack.pop()
        
        

    /*Background*/  g.drawImage(SpriteList.Saturn, 0, 0)
    /*Score*/      g.setFont(FontsLoaded.GemunuLibreLight);g.setFill(Color.GhostWhite);g.fillText("Score: "+gs.Score.toString(), canvas.width.value/3-330, canvas.height.value -750, 300)
    /*Hearts*/     g.drawImage(SpriteList.Heart, 2*canvas.width.value/3 - 500, canvas.height.value - 785);g.setFont(FontsLoaded.GemunuLibreLight);g.setFill(Color.GhostWhite);g.fillText(gs.Lives.toString(), 2*canvas.width.value/3-420, canvas.height.value -750, 300)
    /*WavesCleared*/g.fillText("Waves Cleared: " + gs.WaveCount.toString(), 2*canvas.width.value/3-270, canvas.height.value - 750, 200)
    /*Time*/       g.setFont(FontsLoaded.GemunuLibreLight);g.setFill(Color.GhostWhite);g.fillText("Time: "+gs.Time.toString(), canvas.width.value/2 -375, canvas.height.value - 750, 120)
/*PowerBar*/   g.setFont(FontsLoaded.GemunuLibreExLi);g.setFill(Color.GhostWhite);g.fillText("Rewind Power Remaining: ", 3*canvas.width.value/4 - 100, canvas.height.value-750, 275);g.setFill(Color.DarkRed);g.fillRect(78*canvas.width.value/100, canvas.height.value -780, (250*(rewindcounter/6000.0)), canvas.height.value-763);g.setStroke(Color.SkyBlue);g.strokeRect(78*canvas.width.value/100, canvas.height.value -780, (250*(rewindcounter/6000.0)), canvas.height.value-763)

        gs.Gamer.display(g)
        gs.Swarm.display(g)
        

        for(Bullet <- gs.BulletBuffer){
          Bullet.display(g)
        }
        // println(rewindcounter)
      }


      
      }





         //DISPLAY

       

      })

      timer.start()
      canvas.requestFocus()







    }

  }

}

object FontsLoaded {
  val path1 =
    getClass().getResource("/GemunuLibre/GemunuLibre-Medium.ttf").toString()
  val GemunuLibreMed = Font.loadFont(path1, 25)
  val path2 =
    getClass().getResource("/GemunuLibre/GemunuLibre-ExtraLight.ttf").toString()
  val GemunuLibreExLi = Font.loadFont(path2, 40)
  val path3 =
    getClass().getResource("/GemunuLibre/GemunuLibre-Light.ttf").toString()
  val GemunuLibreLight = Font.loadFont(path3, 40)
  val path4 =
    getClass().getResource("/GemunuLibre/GemunuLibre-Regular.ttf").toString()
  val GemunuLibreReg = Font.loadFont(path4, 40)
  val path5 =
    getClass().getResource("/GemunuLibre/GemunuLibre-SemiBold.ttf").toString()
  val GemunuLibreSB = Font.loadFont(path5, 40)
  val path6 =
    getClass().getResource("/GemunuLibre/GemunuLibre-Bold.ttf").toString()
  val GemunuLibreBold = Font.loadFont(path6, 40)
  val path7 =
    getClass().getResource("/GemunuLibre/GemunuLibre-ExtraBold.ttf").toString
  val GemunuLibreEB = Font.loadFont(path7, 40)

}

object CustomColors {
  val background: Color = Color.hsb(230, 1, 0.1)
  val pausescreenbg: Color = Color.Black
}

object SpriteList {
  val SpaceCraftpath = getClass().getResource("/Sprites/Spacecraft.png")
  val SpaceCraft = new Image(SpaceCraftpath.toString())

  val GamerBulletpath = getClass().getResource("/LaserSprite/BlueLaser.png")
  val GamerBullet = new Image(GamerBulletpath.toString())

  val EnemyBulletpath = getClass().getResource("/LaserSprite/RedLaser.png")
  val EnemyBullet = new Image(EnemyBulletpath.toString())

  val AlienShipPurplepath = getClass().getResource("/Sprites/AlienShipPurple.png")
  val AlienShipPurple = new Image(AlienShipPurplepath.toString())

  val AlienShipWhitepath = getClass().getResource("/Sprites/AlienShipWhite.png")
  val AlienShipWhite = new Image(AlienShipPurplepath.toString())
  
  val WinPicPath = getClass().getResource("/images/Trophy.png")
  val Trophy = new Image(WinPicPath.toString())

  val BackgroundPath = getClass().getResource("/images/Saturn.png")
  val Saturn = new Image(BackgroundPath.toString())
  
  val DeadBackgroundPath = getClass().getResource("/images/deadastronaut.png")
  val DeadBackground = new Image(DeadBackgroundPath.toString())
  
  val HeartPath = getClass().getResource("/images/heart.png")
  val Heart = new Image(HeartPath.toString())
}