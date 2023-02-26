package pt.isec.tp_am

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.auth.User
import org.json.JSONArray
import org.json.JSONObject
import pt.isec.tp_am.databinding.ActivityGameBinding
import pt.isec.tp_am.utils.MaxSecondMax
import pt.isec.tp_am.utils.calculateExpression
import pt.isec.tp_am.utils.prepareStringOperation
import java.io.*
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.properties.Delegates
import kotlin.random.Random


class GameViewModel : ViewModel() {
    lateinit var elementGV : GridView
    private lateinit var context: Context


    private var elementModelArrayList : MutableList<ElementGRVModel> = ArrayList()
    private var levelsList = listOf(levels.OPERATORS)
    var level by Delegates.notNull<Int>()
    private var range by Delegates.notNull<Int>()
    private lateinit var operatators : MutableList<String> //,"-","x","÷"
    var timeFirebase = 0
    var time = 20
        @Synchronized set
    private var timeLevel = 90
    var numberExpressions by Delegates.notNull<Int>()
    private var numberExpressionsLevel = 5
    var operation by Delegates.notNull<String>()
    var max : Double = 0.0
    var secondMax : Double = 0.0
    var points by Delegates.notNull<Int>()
    private var xi = 0
    private var yi = 0
    private var xf = 0
    private var yf = 0
    private lateinit var binding : ActivityGameBinding
    var gameStarted = false
    lateinit var tvPoints: TextView
    lateinit var tvExpresions: TextView
    lateinit var tvLevel: TextView
    var stopGame : Boolean = false
    var pauseGame : Boolean = false
    private lateinit var activity: GameActivity
    private var jsonServer = JSONObject()
    private lateinit var listJsonClients : MutableList<JSONObject>
    private var jsonClient = JSONObject()
    var server = false
    var isMultiplayerGame = false
    private var listMaxSecondMax : MutableMap<String,MaxSecondMax> = mutableMapOf()
    private var listBoardLevel : MutableMap<String,JSONArray> = mutableMapOf()
    private lateinit var board : String
    var clientFinish : Boolean = false
    var serverFinish : Boolean = false
    var timeClient : Int = 90
    var UserName : String? = null
    var pointsClient : Int = 0
    var firstTime = true
    var userPhotoBitmap = null
    var serverUsername = ""
    var timeBackup = 0

    enum class levels {
        OPERATORS, RANGE, NUMBER_EXPRESSIONS
    }

    companion object {
        private const val SWIPE_THRESHOLD = 200
        const val SERVER_PORT = 9999

    }
    enum class State {
        STARTING, UPDATE_UI
    }
    private val _state = MutableLiveData(State.STARTING)
    val state : LiveData<State>
        get() = _state

    private var socket: Socket? = null
    private val socketI: InputStream?
        get() = socket?.getInputStream()
    private val socketO: OutputStream?
        get() = socket?.getOutputStream()
    private var serverSocket: ServerSocket? = null
    private var threadSend: Thread? = null
    private var threadRecive: Thread? = null

    fun createViewModelMultiplayer(ctx: Context, binding: ActivityGameBinding, gameActivity: GameActivity,server: Boolean){
        activity = gameActivity
        context = ctx
        this.binding = binding
        elementGV = binding.idGV
        levelsList += levels.RANGE
        levelsList += levels.NUMBER_EXPRESSIONS
        tvLevel = binding.level
        tvPoints = binding.points
        tvExpresions = binding.expressionsMissing
        tvLevel = binding.level
        tvPoints = binding.points
        tvExpresions = binding.expressionsMissing
        stopGame = false
        pauseGame = false
        isMultiplayerGame = true
        level = 1
        range = 9
        operatators = ArrayList<String>()
        operatators += "+"
        time = 90
        numberExpressions = 5
        operation = ""
        max = 0.0
        secondMax = 0.0
        points = 0
        timeFirebase = 90
        this.server = server
        clientFinish = false
        serverFinish = false
    }

    fun startServer() {
        if(serverSocket != null) return

        thread {
            serverSocket = ServerSocket(SERVER_PORT)
            serverSocket?.run {
                try {
                    val socketClient = serverSocket!!.accept()
                    if(server) {
                        activity.dismissServerDialog()
                        serverComm(socketClient)
                        newServerLevel()

                    }
                } catch (_: Exception) {
                    activity.finish()
                } finally {
                    serverSocket?.close()
                    serverSocket = null
                }
            }
        }
    }

    fun stopServer() {
        serverSocket?.close()
        serverSocket = null
        activity.finish()
    }

    fun startClient(serverIP: String,serverPort: Int = SERVER_PORT){
        if (socket != null)
            return
        thread {
            try {
                val newsocket = Socket()
                newsocket.connect(InetSocketAddress(serverIP,serverPort),5000)
                clientComm(newsocket)

            } catch (_: Exception) {
                stopGame()
                stopServer()
            }
        }
    }

    private fun sendClientInfo() {
        Log.i("PROFILE_INFO", "sendClientInfo IN socketO:[$socketO]")
        if (socketO == null) return

        val map: Map<String, File>? = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            ?.listFiles { file -> file.extension  == "json" }
            ?.associateBy { it.nameWithoutExtension }

        if ((map == null) || map.isEmpty()) {
            Log.i("PROFILE_INFO", "sendClientInfo: NO userInfoJSON FOUND")
            return
        }

        val title = map.keys.toTypedArray()[0]
        val userInfoFile = map[title]?.absolutePath?.let { File(it) }

        // TESTAR SEM userInfoFile JSON FILE // TOAST AO CLICK CLIENT MODE
        val userInfoJSON = JSONObject(userInfoFile!!.bufferedReader().use { it.readText() })
        Log.i("PROFILE_INFO", "JSON FILE: $userInfoJSON")
        val userPhoto = File(userInfoJSON.get("photoImagePath") as String)
        if (userPhoto.exists()) {
            val userPhotoBitmap = BitmapFactory.decodeFile(userPhoto.absolutePath)
            val byteArrayOutputStream = ByteArrayOutputStream()
            userPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
            userInfoJSON.remove("photoImagePath")
            userInfoJSON.put("ProfilePhoto", encodedImage)
            Log.i("PROFILE_INFO", "JSON FILE: $userInfoJSON")
            UserName = userInfoJSON.get("username") as String
        } else
            Toast.makeText(context, "You don't have any profile picture.", Toast.LENGTH_SHORT).show()

        socketO?.run {
            thread {
                try {
                    val printStream = PrintStream(this)
                    printStream.println(userInfoJSON)
                    printStream.flush()
                } catch (_: Exception) {
                    stopGame()
                }
            }
        }
        Log.i("PROFILE_INFO", "sendClientInfo OUT")
    }

    private fun receiveClientInfo(bufferedReaderSocketI : BufferedReader) { /*[PROFILE_INFO]*/
        Log.i("PROFILE_INFO", "receiveClientInfo IN")

        val clientInfoJSON = JSONObject(bufferedReaderSocketI.readLine())
        Log.i("PROFILE_INFO", "receiveClientInfo clientInfoJSON:[${clientInfoJSON}]")

        UserName = clientInfoJSON.get("username") as String
        val userPhotoDecodedByte = Base64.decode(clientInfoJSON.get("ProfilePhoto") as String, Base64.DEFAULT)
        val userPhotoBitmap = BitmapFactory.decodeByteArray(userPhotoDecodedByte, 0, userPhotoDecodedByte.size)

        binding.profilePhoto1.setImageBitmap(userPhotoBitmap) // DEBUG
        /*binding.profileUsername1.text = UserName*/ // DEBUG
        /*binding.profilePoints1.text = "${binding.points.text} Points"*/ // DEBUG

        Log.i("PROFILE_INFO", "receiveClientInfo OUT")
    }

    fun stopGame() {
        try {
            socket?.close()
            socket = null
            threadSend?.interrupt()
            threadSend = null
        } catch (_: Exception) {}
    }


    fun gameOverMultiplayer(){
        var json = JSONObject()
        json.put("level",-1)
        json.put("numberExpressions",-1)
        json.put("time",-1)
        json.put("points",0)
        json.put("operationToVerify",-1)
        json.put("operationCorrectOrWrong",false)
        json.put("operationVerify",false)
        json.put("nBoard",-1)
        json.put("gameOver",true)
        json.put("newLevel",false)
        json.put("finish",false)
        json.put("serverFinished",false)
        socketO?.run {
            thread {
                try {
                    val printStream = PrintStream(this)
                    printStream.println(json)
                    printStream.flush()

                } catch (_: Exception) {
                    stopGame()
                }
            }
        }
        Thread.sleep(1000)
        if(server)
            stopServer()

        activity.dismissServerDialog()
        stopGame()
    }

    private fun newServerLevel() {

        for(i in 0 until numberExpressionsLevel){
            elementModelArrayList.clear()
            var array = JSONArray()
            createBoard()
            for(j in 0 until elementModelArrayList.size) {
                array.put(elementModelArrayList[j].element)
            }
            listMaxSecondMax.put("board${i+1}", MaxSecondMax(max,secondMax))
            listBoardLevel.put("board${i+1}",array)
            jsonServer.put("board${i+1}" ,array)
            max = 0.0
            secondMax = 0.0
        }
        if(firstTime) {
            thread {
                val map: Map<String, File>? =
                    context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                        ?.listFiles { file -> file.extension == "json" }
                        ?.associateBy { it.nameWithoutExtension }

                if ((map == null) || map.isEmpty()) {
                    Log.i("PROFILE_INFO", "sendClientInfo: NO userInfoJSON FOUND")
                }

                val title = map?.keys!!.toTypedArray()[0]
                val userInfoFile = map[title]?.absolutePath?.let { File(it) }

                // TESTAR SEM userInfoFile JSON FILE // TOAST AO CLICK CLIENT MODE
                val userInfoJSON = JSONObject(userInfoFile!!.bufferedReader().use { it.readText() })
                serverUsername = userInfoJSON.get("username") as String
                Log.i("Username", UserName.toString())
            }
            firstTime = false
        }

        jsonServer.put("level",level)
        jsonServer.put("numberExpressions",numberExpressionsLevel)
        jsonServer.put("time",timeClient)
        jsonServer.put("points",0)
        jsonServer.put("operationToVerify",-1)
        jsonServer.put("operationCorrectOrWrong",false)
        jsonServer.put("operationVerify",false)
        jsonServer.put("nBoard","board" + 1)
        jsonServer.put("gameOver",false)
        jsonServer.put("newLevel",true)
        jsonServer.put("finish",false)
        jsonServer.put("serverFinished",false)

        jsonClient = jsonServer

        socketO?.run {
            thread {
                try {
                    Thread.sleep(1000)
                    val printStream = PrintStream(this)
                    printStream.println(jsonClient)
                    printStream.flush()
                    _state.postValue(State.UPDATE_UI)

                } catch (_: Exception) {
                    stopGame()
                }
            }
        }

        board = "board1"

        Log.i("Aqui!","Aqui!")
        serverGameBoard()
    }

    private fun serverGameBoard() {
        if(listBoardLevel.containsKey(board)) {
            elementModelArrayList.clear()
            val list = ArrayList<String>()
            val jArray = listBoardLevel[board]
            if (jArray != null) {
                for (i in 0 until jArray.length()) {
                    list.add(jArray.getString(i))
                }
            }
            for (i in 0 until list.size)
                elementModelArrayList.add(ElementGRVModel(list[i]))
        }
        activity.dismissServerDialog()
    }

    private fun changeLevelServer(op : Double) {
        thread {
            _state.postValue(State.STARTING)
            var aux = listMaxSecondMax[board]
            if(aux != null){
                if(aux.max == op){
                    var auxTime = time
                    auxTime += 5
                    if(auxTime < timeLevel)
                        time = auxTime
                    numberExpressions--
                    points += 2

                    if(numberExpressions == 0)
                        serverFinish = true
                    else {
                        var nBoard = board[board.length - 1]
                        nBoard++
                        board = "board$nBoard"
                        serverGameBoard()
                    }
                    var json = JSONObject()
                    json.put("level",-1)
                    json.put("numberExpressions",-1)
                    json.put("time",-1)
                    json.put("points",-1)
                    json.put("operationToVerify",-1)
                    json.put("operationCorrectOrWrong",false)
                    json.put("operationVerify",false)
                    json.put("nBoard",-1)
                    json.put("gameOver",false)
                    json.put("newLevel",false)
                    json.put("serverFinished",serverFinish)
                    json.put("finish",clientFinish)
                    json.put(board, -1)
                    socketO?.run {
                        thread {
                            val printStream = PrintStream(this)
                            printStream.println(json)
                            printStream.flush()
                        }
                    }


                }else if(aux.secondMax == op){
                    points++
                }else{
                    Log.i("board",board)
                    numberExpressions--
                    if(numberExpressions == 0) {
                        serverFinish = true
                    }
                    var json = JSONObject()
                    json.put("level",-1)
                    json.put("numberExpressions",-1)
                    json.put("time",-1)
                    json.put("points",-1)
                    json.put("operationToVerify",-1)
                    json.put("operationCorrectOrWrong",false)
                    json.put("operationVerify",false)
                    json.put("nBoard",-1)
                    json.put("gameOver",false)
                    json.put("newLevel",false)
                    json.put("serverFinished",serverFinish)
                    json.put("finish",clientFinish)
                    json.put(board, -1)
                    socketO?.run {
                        thread {
                            val printStream = PrintStream(this)
                            printStream.println(json)
                            printStream.flush()
                        }
                    }

                }
            }
            _state.postValue(State.UPDATE_UI)
        }

    }


    fun newLevelMultiplayer(){
        level++
        newLevel()
        newServerLevel()
        clientFinish = false
        serverFinish = false

    }


    private fun clientComm(newSocket: Socket) {
        activity.dismissServerDialog()
        socket = newSocket

        sendClientInfo()

        threadRecive = thread {

            try {
                if(socketI == null)
                    return@thread

                val bufI = socketI!!.bufferedReader()

                while (true){
                    if(!server) {
                        var jsonObject = JSONObject(bufI.readLine())
                        jsonClient = jsonObject
                        _state.postValue(State.STARTING)

                        var newLevel = jsonClient.get("newLevel") as Boolean
                        var gameOver = jsonClient.get("gameOver") as Boolean

                        var operationVerify = jsonClient.get("operationVerify") as Boolean
                        var finished = jsonClient.get("finish") as Boolean
                        clientFinish = finished

                        var serverFinished = jsonClient.get("serverFinished") as Boolean
                        serverFinish = serverFinished
                        var numberExpressionsClient = jsonClient.get("numberExpressions") as Int

                        if(numberExpressionsClient != -1)
                            numberExpressions = numberExpressionsClient

                        if(gameOver){
                            activity.gameOverClientServer()
                        }

                        if(newLevel && !finished){
                            activity.dismissServerDialog()
                            level = jsonClient.get("level") as Int
                            time = jsonClient.get("time") as Int

                            elementModelArrayList.clear()
                            val list = ArrayList<String>()
                            board = jsonObject.get("nBoard") as String
                            val jArray : JSONArray = jsonClient.getJSONArray(board)
                            if (jArray != null) {
                                for (i in 0 .. jArray.length()-1) {
                                    list.add(jArray.getString(i))
                                }
                            }
                            for (i in 0 .. list.size - 1)
                                elementModelArrayList.add(ElementGRVModel(list[i]))

                            var aux = time - timeBackup
                            if(aux == 5)
                                timeFirebase += aux

                        }else if(operationVerify && !finished){
                            time = jsonClient.get("time") as Int
                            elementModelArrayList.clear()
                            val list = ArrayList<String>()
                            Log.i("boardTeste",board)
                            board = jsonObject.get("nBoard") as String
                            Log.i("boardTeste",board)
                            val jArray : JSONArray = jsonClient.getJSONArray(board)
                            if (jArray != null) {
                                for (i in 0 .. jArray.length()-1) {
                                    list.add(jArray.getString(i))
                                }
                            }
                            for (i in 0 .. list.size - 1)
                                elementModelArrayList.add(ElementGRVModel(list[i]))
                        }

                        var pointsAux = jsonObject.get("points") as Int
                        if(pointsAux != -1)
                            points = pointsAux
                        _state.postValue(State.UPDATE_UI)
                        if(firstTime){
                            firstTime = false
                        }

                    }
                }
            }catch (_: Exception){

            }finally {
                stopGame()
            }
        }


    }

    private fun sendToVerifyOperation(op: String) {
        if(socketO == null)
            return

        var json = JSONObject()
        json.put("level",level)
        json.put("numberExpressions",numberExpressions)
        json.put("time",time)
        json.put("points",points)
        json.put("operationToVerify",op)
        json.put("operationCorrectOrWrong",false)
        json.put("operationVerify",true)
        json.put("nBoard",board)
        json.put("gameOver",false)
        json.put("newLevel",false)
        json.put("serverFinished",serverFinish)
        json.put("finish",clientFinish)
        timeBackup = time

        socketO?.run {
            thread {
                val printStream = PrintStream(this)
                printStream.println(json)
                printStream.flush()
            }
        }

    }


    private fun serverComm(newSocket: Socket?) {
        socket = newSocket
        threadRecive = thread {
            try {
                if (socketI == null) return@thread

                val bufI = socketI!!.bufferedReader()

                Thread.sleep(900) // 1000
                Log.i("PROFILE_INFO", "INVOKE receiveClientInfo")
                receiveClientInfo(bufI)

                while (true) {
                    var jsonObject = JSONObject(bufI.readLine())
                    var operationVerify = jsonObject.get("operationVerify") as Boolean
                    var clientFinished = jsonObject.get("finish") as Boolean
                    clientFinish = clientFinished
                    var gameOver = jsonObject.get("gameOver") as Boolean

                    if(gameOver){
                        activity.gameOverClientServer()
                    }
                    _state.postValue(State.STARTING)


                    if(operationVerify){
                        var board = jsonObject.get("nBoard") as String
                        var aux = listMaxSecondMax[board]
                        if(aux != null){
                            var operationToVerify = jsonObject.get("operationToVerify") as String
                            var calc = calculateExpression(operationToVerify)

                            if(calc == aux.max) {
                                var numberExpressionsClient : Int
                                var pointsClient : Int
                                var aux = jsonObject.get("time") as Int
                                aux += 5
                                if(aux < timeLevel)
                                    timeClient = aux
                                else
                                    timeClient = jsonObject.get("time") as Int
                                numberExpressionsClient = jsonObject.get("numberExpressions") as Int
                                numberExpressionsClient--
                                pointsClient = jsonObject.get("points") as Int
                                pointsClient += 2

                                var nBoard = board[board.length-1]
                                nBoard++
                                var json = JSONObject()
                                if(listMaxSecondMax.contains("board$nBoard")){
                                    json.put("nBoard","board$nBoard")
                                    board = "board$nBoard"
                                }else
                                    json.put("nBoard",board)

                                if(numberExpressionsClient == 0) {
                                    json.put("finish", true)
                                    clientFinish = true
                                }else
                                    json.put("finish",false)

                                json.put("level",jsonObject.get("level"))
                                json.put("numberExpressions",numberExpressionsClient)
                                json.put("time",timeClient)
                                json.put("points",pointsClient)
                                json.put("operationToVerify",-1)
                                json.put("operationCorrectOrWrong",false)
                                json.put("operationVerify",true)
                                json.put("nBoard",board)
                                json.put("gameOver",false)
                                json.put("newLevel",false)
                                json.put("serverFinished",serverFinish)
                                json.put("pointsServer",points)

                                if(listBoardLevel.containsKey(board)){
                                    json.put(board, listBoardLevel[board])
                                }

                                this.pointsClient = pointsClient

                                _state.postValue(State.STARTING)
                                _state.postValue(State.UPDATE_UI)
                                socketO?.run {
                                    thread {
                                        val printStream = PrintStream(this)
                                        printStream.println(json)
                                        printStream.flush()
                                    }
                                }


                            }else if(calc == aux.secondMax){
                                var pointsClient = jsonObject.get("points") as Int
                                pointsClient++
                                timeClient = jsonObject.get("time") as Int
                                var json = JSONObject()
                                json.put("level",jsonObject.get("level"))
                                var n = jsonObject.get("numberExpressions")
                                json.put("numberExpressions",n)
                                json.put("time",timeClient)
                                json.put("points",pointsClient)
                                json.put("operationToVerify",-1)
                                json.put("operationCorrectOrWrong",true)
                                json.put("operationVerify",true)
                                var board = jsonObject.get("nBoard")
                                if(listBoardLevel.containsKey(board)){
                                    Log.i("TesteBoard",board.toString())
                                    json.put(board.toString(),listBoardLevel[board])
                                }
                                json.put("nBoard",board)
                                json.put("gameOver",false)
                                json.put("newLevel",false)
                                json.put("finish",false)
                                json.put("serverFinished",serverFinish)
                                json.put("pointsServer",points)
                                this.pointsClient = pointsClient

                                _state.postValue(State.STARTING)
                                _state.postValue(State.UPDATE_UI)

                                socketO?.run {
                                    thread {
                                        val printStream = PrintStream(this)
                                        printStream.println(json)
                                        printStream.flush()
                                    }
                                }
                            }else{
                                Log.i("Calc", "ELSE")
                                var json = JSONObject()
                                json.put("level",jsonObject.get("level"))
                                var numberExpressionsClient = jsonObject.get("numberExpressions") as Int
                                numberExpressionsClient--
                                timeClient = jsonObject.get("time") as Int
                                json.put("numberExpressions",numberExpressionsClient)
                                json.put("time",timeClient)
                                json.put("points",jsonObject.get("points"))
                                json.put("operationToVerify",-1)
                                json.put("operationCorrectOrWrong",false)
                                json.put("operationVerify",true)
                                var board = jsonObject.get("nBoard") as String
                                if(listBoardLevel.containsKey(board)){
                                    Log.i("TesteBoard",board)
                                    json.put(board,listBoardLevel[board])
                                }
                                if(numberExpressionsClient == 0) {
                                    json.put("finish", true)
                                    clientFinish = true
                                }else
                                    json.put("finish",false)
                                json.put("nBoard",board)
                                json.put("gameOver",false)
                                json.put("newLevel",false)
                                json.put("serverFinished",serverFinish)
                                json.put("pointsServer",points)
                                Log.i("numberExpressionsClient", numberExpressionsClient.toString())


                                socketO?.run {
                                    thread {
                                        val printStream = PrintStream(this)
                                        printStream.println(json)
                                        printStream.flush()
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (_: Exception){

            }finally {
                stopGame()
            }

                _state.postValue(State.UPDATE_UI)
        }

    }



    //----------------------------------------Single Player----------------------------------------//
    fun createViewModel(ctx: Context, binding: ActivityGameBinding, gameActivity: GameActivity){
        activity = gameActivity
        context = ctx
        this.binding = binding
        elementGV = binding.idGV
        levelsList += levels.RANGE
        levelsList += levels.NUMBER_EXPRESSIONS
        tvLevel = binding.level
        tvPoints = binding.points
        tvExpresions = binding.expressionsMissing
        if(!gameStarted) {
            startGame()
            gameStarted = true
        }else{
            initializeBoard()
        }
        updateUi()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initializeBoard() {


        val adapter = ElementGVAdapter(elementModelArrayList,context)
        elementGV.adapter = adapter


        elementGV.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if(event.action == KeyEvent.ACTION_DOWN) {
                    xi = event.x.toInt()
                    yi = event.y.toInt()
                    return true
                }
                if(event.action == KeyEvent.ACTION_UP){

                    xf = event.x.toInt()
                    yf = event.y.toInt()

                    val diffY = yf - yi
                    val diffX = xf - xi

                    if (abs(diffX) > abs(diffY)) {
                        if (abs(diffX) > SWIPE_THRESHOLD) {
                            var aux1 = yi + 200
                            var aux2 = yi - 200
                            if (diffX != 0 && (yf in (aux2 + 1) until aux1)) {
                                if(isMultiplayerGame){

                                }
                                onSwipeRightLeft()
                            }
                        }
                    } else {
                        if (abs(diffY) > SWIPE_THRESHOLD) {
                            var aux1 = xi + 200
                            var aux2 = xi - 200
                            if (diffY != 0 && (xf in (aux2 + 1) until aux1)) {
                                onSwipeBottomTop()
                            }
                        }
                    }


                    return true
                }
                return false
            }
        })
    }

    fun startGame(){
        elementModelArrayList = ArrayList<ElementGRVModel>()
        initialize()
        initializeBoard()
        createBoard()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialize(){

        tvLevel = binding.level
        tvPoints = binding.points
        tvExpresions = binding.expressionsMissing
        stopGame = false
        pauseGame = false
        level = 1
        range = 9
        operatators = ArrayList<String>()
        operatators += "+"
        time = 90
        numberExpressions = 5
        operation = ""
        max = 0.0
        secondMax = 0.0
        points = 0
        timeFirebase = 90

        val map: Map<String, File>? = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            ?.listFiles { file -> file.extension  == "json" }
            ?.associateBy { it.nameWithoutExtension }

        if ((map == null) || map.isEmpty()) {
            Log.i("PROFILE_INFO", "sendClientInfo: NO userInfoJSON FOUND")
            return
        }

        val title = map.keys.toTypedArray()[0]
        val userInfoFile = map[title]?.absolutePath?.let { File(it) }

        // TESTAR SEM userInfoFile JSON FILE // TOAST AO CLICK CLIENT MODE
        val userInfoJSON = JSONObject(userInfoFile!!.bufferedReader().use { it.readText() })
        UserName = userInfoJSON.get("username") as String


        updateUi()

    }

    private fun updateUi(){
        tvPoints.text = " $points ${context.getString(R.string.points)}"
        tvExpresions.text = " $numberExpressions ${context.getString(R.string.expressions_missing)}"
        tvLevel.text = level.toString()
    }


    private fun onSwipeRightLeft(){
        val initialPosition = elementGV.pointToPosition(xi, yi)
        if(elementGV.pointToPosition (xf, yf) != -1) {
            val finalPosition = elementGV.pointToPosition(xf, yf)

            operation = ""
            if(finalPosition < initialPosition) {
                for (i in finalPosition..initialPosition) {
                    operation += elementModelArrayList[i].getElementGRV()
                }
            }else{
                for (i in initialPosition..finalPosition) {
                    operation += elementModelArrayList[i].getElementGRV()
                }
            }
            if(!operation.contains(" ")) {
                var s = prepareStringOperation(operation)
                if(s != "NA") {
                    if (isMultiplayerGame && !server){
                        sendToVerifyOperation(s)
                    }else
                        verifyOperation(calculateExpression(s))
                }
            }
        }

    }

    private fun onSwipeBottomTop(){
        val initialPosition = elementGV.pointToPosition(xi, yi)
        if(elementGV.pointToPosition (xf, yf) != -1) {
            val finalPosition = elementGV.pointToPosition(xf, yf)

            operation = ""
            if(finalPosition < initialPosition) {
                for (i in finalPosition..initialPosition step 5) {
                    operation += elementModelArrayList[i].getElementGRV()
                }
            }else{
                for (i in initialPosition..finalPosition step 5) {
                    operation += elementModelArrayList[i].getElementGRV()
                }
            }
            var s = prepareStringOperation(operation)
            if(s != "NA") {
                if (isMultiplayerGame && !server){
                    sendToVerifyOperation(s)
                }else {
                    verifyOperation(calculateExpression(s))
                }
            }
        }
    }



    private fun createBoard() {
        for (i in 0..2) {
            for (j in 0..4){
                if((j % 2) == 0) {
                    val numbers = Random.nextInt(1, range).toString()
                    elementModelArrayList.add(ElementGRVModel(numbers))
                }else{
                    val operator = Random.nextInt(0, operatators.size)
                    elementModelArrayList.add(ElementGRVModel(operatators[operator]))
                }
            }
            if(i != 2) {
                for (j in 0..4) {
                    if ((j % 2) == 0) {
                        val operator = Random.nextInt(0, operatators.size)
                        elementModelArrayList.add(ElementGRVModel(operatators[operator]))
                    } else {
                        elementModelArrayList.add(ElementGRVModel(" "))
                    }
                }
            }
        }

        calcMaxAndSecMax()
    }

    private fun calcMaxAndSecMax() {
        for(i in 0 .. 4){ // linhas completas
            var op = ""
            for (j in 0 .. 4){
                var pos = (i * 5) + j
                op += elementModelArrayList[pos].getElementGRV()
            }
            calculate(op)
        }
        for(i in 0 .. 4){ // colunas completas
            var op = ""
            for(j in 0 .. 4){
                var pos = (j * 5) + i
                op += elementModelArrayList[pos].getElementGRV()
            }
            calculate(op)
        }
        for(i in 0 .. 4){ // linhas 2 a 2
            var op = ""
            var x = 2
            for(j in 0 .. x){
                var pos = (i * 5) + j
                op += elementModelArrayList[pos].getElementGRV()
            }
            calculate(op)
            op = ""
            x += 2
            for(j in 2 .. x){
                var pos = (i * 5) + j
                op += elementModelArrayList[pos].getElementGRV()
            }
            calculate(op)
        }
        for(i in 0 .. 4){ // colunas 2 a 2
            var op = ""
            var x = 2
            for(j in 0 .. x){
                var pos = (j * 5) + i
                op += elementModelArrayList[pos].getElementGRV()
            }
            calculate(op)
            op = ""
            x += 2
            for(j in 2 .. x){
                var pos = (j * 5) + i
                op += elementModelArrayList[pos].getElementGRV()
            }
            calculate(op)
        }
    }

    private fun calculate(s : String){
        var op = prepareStringOperation(s)
        if(op != "NA") {
            val aux = calculateExpression(prepareStringOperation(op))
            if (aux > max) {
                if(max != 0.0)
                    secondMax = max
                max = aux
            }else if(aux > secondMax){
                secondMax = aux
            }
        }
    }

    private fun verifyOperation(op : Double){
        if(isMultiplayerGame)
            changeLevelServer(op)
        else if(op == max) {
            var aux = time
            aux += 5
            if(aux < timeLevel) {
                time = aux
                timeFirebase += 5
            }
            numberExpressions--
            points += 2
            tvPoints.text = " " + points + " " + context.getString(R.string.points)
            tvExpresions.text = " " + numberExpressions + " " + context.getString(R.string.expressions_missing)
            changeLevel()
        } else if(op == secondMax){
            points++
            tvPoints.text = " " + points + " " + context.getString(R.string.points)
        }else{
            createNewBorad()
        }

    }



    private fun createNewBorad(){
        max = 0.0
        secondMax = 0.0
        elementModelArrayList.clear()
        elementGV.adapter = ElementGVAdapter(elementModelArrayList,context)
        createBoard()
    }

    private fun changeLevel(){
        if(numberExpressions != 0){
            updateUi()
            createNewBorad()
        }else{
            level++
            stopLevel()
        }

    }

    fun alertDialogStopGame(): AlertDialog? {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.new_level))
            .setMessage(context.getString(R.string.level_dialog) + " ${level}! \n " + context.getString(R.string.pause_game_dialog))
            .setPositiveButton(context.getString(R.string.yes)){ _: DialogInterface, _: Int ->
                stopGame = true
                pauseGame = true
                alertDialogResumeGame()
            }
            .setNegativeButton(context.getString(R.string.no)) { _: DialogInterface, _: Int ->
                stopGame = false

            }
            .setOnKeyListener(DialogInterface.OnKeyListener { arg0, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    activity.onBackPressed()
                }
                true
            })
            .setCancelable(true)
            .create()
        return alertDialog
    }
    fun alertDialogResumeGame() {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.new_level))
            .setMessage(context.getString(R.string.level_dialog) + " ${level}! \n " + context.getString(R.string.pause_resume_dialog))
            .setPositiveButton(context.getString(R.string.yes)){ _: DialogInterface, _: Int ->
                stopGame = false
                pauseGame = false
            }
            .setOnKeyListener(DialogInterface.OnKeyListener { arg0, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    activity.onBackPressed()
                }
                true
            })
            .setCancelable(true)
            .create()
            .show()
    }

    private fun stopLevel() {
        val alertDialog = alertDialogStopGame()
        if (alertDialog != null) {
            alertDialog.show()
        }
        if (alertDialog != null) {
            val flag = false
            stopGameThread(alertDialog)
        }
        newLevel()
        createNewBorad()
        updateUi()
    }
    val newLevelStop = Executors.newSingleThreadExecutor()
    private fun stopGameThread(alertDialog: AlertDialog) {
        repeat(1) {
            newLevelStop.execute {
                stopGame = true
                Thread.sleep(5000)
                if(!pauseGame) {
                    stopGame = false
                    alertDialog.dismiss()
                }
            }
        }
    }

    var limitRange = 3
    private fun newLevel(){
        val value = Random.nextInt(0, levelsList.size)
        when (levelsList[value]){
            levels.OPERATORS -> {
                var aux = operatators[operatators.size-1]
                when (aux){
                    "+" ->{
                        operatators += "-"
                        numberExpressions = numberExpressionsLevel
                    }
                    "-" -> {
                        operatators += "x"
                        numberExpressions = numberExpressionsLevel
                    }
                    "x" ->{
                        levelsList -= levels.OPERATORS
                        operatators += "÷"
                        numberExpressions = numberExpressionsLevel
                    }

                }
            }
            levels.RANGE -> {
                var aux = range.toString()
                aux += "9"
                range = aux.toInt()
                numberExpressions = numberExpressionsLevel
                limitRange--
                if(limitRange == 0)
                    levelsList -= levels.RANGE
            }
            else -> {
                numberExpressions = Random.nextInt(0, 5)
                numberExpressions += numberExpressionsLevel
                var keepgoing = true
                while (keepgoing) {
                    var value = Random.nextInt(0, levelsList.size)
                    when (levelsList[value]) {
                        levels.OPERATORS -> {
                            var aux = operatators[operatators.size - 1]
                            when (aux) {
                                "+" -> {
                                    operatators += "-"
                                    numberExpressions = numberExpressionsLevel
                                    keepgoing = false
                                }
                                "-" -> {
                                    operatators += "x"
                                    numberExpressions = numberExpressionsLevel
                                    keepgoing = false
                                }
                                "x" -> {
                                    levelsList -= levels.OPERATORS
                                    operatators += "÷"
                                    numberExpressions = numberExpressionsLevel
                                    keepgoing = false
                                }

                            }
                        }
                        levels.RANGE -> {
                            var aux = range.toString()
                            aux += "9"
                            range = aux.toInt()
                            numberExpressions = numberExpressionsLevel
                            keepgoing = false
                            limitRange--
                            if(limitRange == 0)
                                levelsList -= levels.RANGE
                        }
                        else -> {}
                    }
                }
            }
        }
        if(!isMultiplayerGame)
            updateUi()
    }



}