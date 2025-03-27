package cs2.game


import scala.collection.mutable.Buffer
import scala.collection.mutable.Set
import cs2.util.Vec2
import scala.collection.mutable.Stack

class GameState() {

var Gamer = new Player(SpriteList.SpaceCraft, new Vec2(615, 720), SpriteList.GamerBullet) 
var GamerBullet = new Bullet(SpriteList.GamerBullet, new Vec2(Gamer.initPos.x, Gamer.initPos.y + 100),Vec2(0, 2.5))
var Swarm = new EnemySwarm(6,3)
val totalEnemiesPerWave = 18

var BulletBuffer = Buffer[Bullet]()
//var EnemySwarmBuffer = Buffer[Enemy]()
var ScoreDisplay = false
var LivesDisplay = false
var TimeDisplay = false
var waveDisplay = false
var Score:Int = 0
var Lives:Int = 5
var Time: Int = 100
var count:Int = 0
var WaveCount = 0
var waveClearTimer: Int = 0
var killcounter = 0
var rewindcounter = 0
var bulletinterception = 0

def calculateShootingSpeedFactor(): Double = {
    val remainingEnemies = Swarm.EnemyBuffer.length
    val totalEnemies = totalEnemiesPerWave
    val waveMultiplier = WaveCount
    val waveWeight = 1.0
    val enemyWeight = 0.05
    val shootingSpeedFactor = (1.0+waveMultiplier*waveWeight)*(1.0+enemyWeight*(totalEnemies-remainingEnemies))
    return  shootingSpeedFactor
}

def calculateEnemySpeedMultiplier(): Double = {
    val remainingEnemies = Swarm.EnemyBuffer.length
    val totalEnemies = totalEnemiesPerWave
    val waveMultiplier = WaveCount
    val waveWeight = 0.5
    val enemyWeight = 0.05
    val movementSpeedMultiplier = (1.0+waveMultiplier*waveWeight)*(1.0+enemyWeight*(totalEnemies-remainingEnemies))
    // println("movementspeedmultiplier: " + movementSpeedMultiplier)
    return  movementSpeedMultiplier
}
def deepcopy():GameState = {

    var ClonedState:GameState = new GameState()

    ClonedState.Gamer = this.Gamer.clone()
    ClonedState.Swarm.EnemyBuffer = Swarm.clonedbuffer()



    ClonedState.BulletBuffer = Buffer[Bullet]()
    for(Bullet <- this.BulletBuffer){
        var aBullet = Bullet.clone()
    ClonedState.BulletBuffer+= aBullet
    }
    


    
    ClonedState.Lives = this.Lives
    ClonedState.Time = this.Time
    ClonedState.Score = this.Score
    ClonedState.WaveCount = this.WaveCount

    ClonedState.count = this.count
    ClonedState.killcounter = this.killcounter
    ClonedState.bulletinterception = this.bulletinterception


    //ClonedState.EnemySwarmBuffer = this.EnemySwarmBuffer
    

    return ClonedState
}
}