package pt.isec.tp_am

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Environment
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.tp_am.databinding.ActivityGameBinding
import java.io.File
import java.util.concurrent.Executors
import kotlin.concurrent.thread


class GameActivity : AppCompatActivity() {
    companion object {
        private var SERVER_MODE = 0
        private var CLIENT_MODE = 1
        private var SINGLE_PLAYER = 2

        fun getServerModeIntent(context : Context) : Intent {
            return Intent(context,GameActivity::class.java).apply {
                putExtra("mode", SERVER_MODE)
            }
        }

        fun getClientModeIntent(context : Context) : Intent {
            return Intent(context,GameActivity::class.java).apply {
                putExtra("mode", CLIENT_MODE)
            }
        }
        fun getSinglePlayerIntent(context : Context) : Intent {
            return Intent(context,GameActivity::class.java).apply {
                putExtra("mode", SINGLE_PLAYER)
            }
        }
    }

    private val model : GameViewModel by viewModels()
    private lateinit var binding : ActivityGameBinding
    private lateinit var tvTime: TextView
    private var dlg: AlertDialog? = null
    private lateinit var threadTime : Thread
    private var continueThread : Boolean = false
    private var gameOver = false
    private var alertDialog: AlertDialog? = null

    private var listenerRegistration_1 : ListenerRegistration? = null
    private var listenerRegistration_2 : ListenerRegistration? = null
    enum class CollectionPath {
        SoloScores, MultiplayerPointScores, MultiplayerTimeScores
    } // private var collectionPath : String = ""
    private var top5 = HashMap<String, HashMap<String?,String?>>()
    private var top5Time = HashMap<String, HashMap<String?,String?>>()
    private var top5Points = HashMap<String, HashMap<String?,String?>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        tvTime = binding.seconds
        perfil()

        /*****************************************************************************************************/
        //collectionPath = "SoloScores"
        /*addDataToFirestore(1, hashMapOf(
            "points" to 34,
            "time" to 56
        ))*/
        //startObserverSinglePlayer()

        /*collectionPath = "MultiplayerPointScores"
        addDataToFirestore(1, hashMapOf(
            "points" to 88
        ))*/
        //startObserverMultiplayerPoints()

        /*collectionPath = "MultiplayerTimeScores"
        addDataToFirestore(1, hashMapOf(
            "time" to 58
        ))*/
        //updateDataInFirestoreTrans(CollectionPath.MultiplayerTimeScores, 1, hashMapOf("time" to 99))
        //startObserverMultiplayerTime(CollectionPath.MultiplayerTimeScores)
        /*****************************************************************************************************/

        when(intent.getIntExtra("mode", SERVER_MODE)) {
            SERVER_MODE -> {
                model.createViewModelMultiplayer(this,binding,this@GameActivity,true)
                startObserverMultiplayerPoints(CollectionPath.MultiplayerPointScores)
                startObserverMultiplayerTime(CollectionPath.MultiplayerTimeScores)
                startAsServer()
            }
            CLIENT_MODE -> {
                model.createViewModelMultiplayer(this,binding,this@GameActivity,false)
                startObserverMultiplayerPoints(CollectionPath.MultiplayerPointScores)
                startObserverMultiplayerTime(CollectionPath.MultiplayerTimeScores)
                startAsClient()

            }
            else -> {
                val ll = binding.linearLayout
                val ll2 = binding.linearLayout2
                ll.removeView(ll2)
                model.createViewModel(this, binding, this@GameActivity)
                continueThread = true
                startObserverSinglePlayer(CollectionPath.SoloScores)
                changeTime()
            }
        }

        model.state.observe(this) {
            updateUI()
        }
    }

    private fun perfil(){
        val map: Map<String, File>? = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            ?.listFiles { file -> file.extension  == "json" }
            ?.associateBy { it.nameWithoutExtension }

        if ((map == null) || map.isEmpty()) {
            Log.i("PROFILE_INFO", "sendClientInfo: NO userInfoJSON FOUND")
            Toast.makeText(this, getString(R.string.toast_profile), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onPause() {
        super.onPause()
        continueThread = false
        dlg?.dismiss()
        stopObserver()
    }

    override fun onStop() {
        super.onStop()
        continueThread = true
        if(model.isMultiplayerGame)
            model.gameOverMultiplayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        continueThread = false
        if(gameOver) {
            model.gameOverMultiplayer()
            Toast.makeText(this, getString(R.string.game_over_dialog), Toast.LENGTH_LONG).show()
        }
    }

    /**********************************************************************************/
    fun addDataToFirestore(collectionPath : CollectionPath, positionTop : Int, scores : HashMap<String?, String?>) {
        if (positionTop !in 1..5) return

        val db = Firebase.firestore
        db.collection(collectionPath.toString()).document("Top${positionTop}").set(scores)
            .addOnSuccessListener {
                Log.i("FIREBASE", "addDataToFirestore: Success")
            }
            .addOnFailureListener { e ->
                Log.i("FIREBASE", "addDataToFirestore: Failure, ${e.message}")
            }
    }

    //////////////////////////////////////////////////////////////////////
    private fun updateDataInFirestoreTrans(collectionPath: CollectionPath, positionTop: Int, scores: HashMap<String?, String?>) {
        if (positionTop !in 1..5) return

        val db = Firebase.firestore
        val v = db.collection(collectionPath.toString()).document("Top${positionTop}")
        db.runTransaction { transaction ->
            val doc = transaction.get(v)
            if (doc.exists() && scores!!.isNotEmpty()) {
                if (scores["points"] != null) {
                    val newPoints = scores["points"]!!
                    transaction.update(v, "points", newPoints)
                }
                if (scores["time"] != null) {
                    val newTime = scores["time"]!!
                    transaction.update(v, "time", newTime)
                }
                if (scores["username"] != null) {
                    val newTime = scores["username"]!!
                    transaction.update(v, "username", newTime)
                }
                null
            } else
                throw FirebaseFirestoreException(
                    "Error", FirebaseFirestoreException.Code.UNAVAILABLE
                )
        }.addOnSuccessListener {
            Log.i("FIREBASE", "updateDataInFirestoreTrans: Success")
        }.addOnFailureListener { e ->
            Log.i("FIREBASE", "updateDataInFirestoreTrans: Failure, ${e.message}")
        }
    }

    //////////////////////////////////////////////////////////////////////
    private fun removeDataFromFirestore(collectionPath : CollectionPath, positionTop : Int) {
        if (positionTop !in 1..5) return

        val db = Firebase.firestore
        val v = db.collection(collectionPath.toString()).document("Top${positionTop}")
        v.delete()
            .addOnSuccessListener {
                Log.i("FIREBASE", "removeDataFromFirestore: Success")
            }
            .addOnFailureListener { e ->
                Log.i("FIREBASE", "removeDataFromFirestore: Failure, ${e.message}")
            }
    }
    /**********************************************************************************/

    /**********************************************************************************/
    private fun startObserverSinglePlayer(collectionPath : CollectionPath) {
        val db = Firebase.firestore

        for (i in 1..5) {
            listenerRegistration_1 = db.collection(collectionPath.toString()).document("Top${i}")
                .addSnapshotListener { document, e ->
                    if (e != null) return@addSnapshotListener
                    if (document != null && document.exists()) {
                        top5["Top${i}"] = hashMapOf(
                            "username" to document.getString("username"),
                            "points" to document.getString("points"),
                            "time" to document.getString("time"),
                        )
                        Log.i("FIREBASE","getDataInFirestore: top5 $top5")
                    }
                }
        }
    }
    private fun startObserverMultiplayerTime(collectionPath : CollectionPath) {
        val db = Firebase.firestore

        for (i in 1..5) {
            listenerRegistration_1 = db.collection(collectionPath.toString()).document("Top${i}")
                .addSnapshotListener { document, e ->
                    if (e != null) return@addSnapshotListener
                    if (document != null && document.exists()) {
                        top5Time["Top${i}"] = hashMapOf(
                            "username" to document.getString("username"),
                            "time" to document.getString("time")
                        )
                        Log.i("FIREBASE","getDataInFirestore: top5 $top5Time")
                    }
                }
        }
    }

    private fun startObserverMultiplayerPoints(collectionPath : CollectionPath) {
        val db = Firebase.firestore

        for (i in 1..5) {
            listenerRegistration_2 = db.collection(collectionPath.toString()).document("Top${i}")
                .addSnapshotListener { document, e ->
                    if (e != null) return@addSnapshotListener
                    if (document != null && document.exists()) {
                        top5Points["Top${i}"] = hashMapOf(
                            "username" to document.getString("username"),
                            "points" to document.getString("points")
                        )

                        Log.i("FIREBASE","getDataInFirestore: top5 $top5Points")
                    }
                }
        }
    }

    private fun stopObserver() {
        listenerRegistration_1?.remove()
        listenerRegistration_2?.remove()
    }
    /**********************************************************************************/

    override fun onBackPressed() {
        alertDialog = AlertDialog.Builder(this)
            .setMessage(getString(R.string.leave_game_dialog))
            .setPositiveButton(getString(R.string.yes)) { _: DialogInterface, _: Int ->
                if(model.isMultiplayerGame)
                    model.gameOverMultiplayer()
                finish()
            }
            .setNegativeButton(getString(R.string.no)) { _: DialogInterface, _: Int -> }
            .setCancelable(true)
            .create()

        alertDialog?.show()
    }


    fun gameOverClientServer(){
        continueThread = false
        gameOver = true
        updateFirebaseMultiplayer()
        this.finish()
        return
    }


    fun updateFirebaseMultiplayer(){
        var mapFinal = HashMap<String,HashMap<String?,String?>>()
        var j = 1
        var keepGoing = true

        var username : String = model.UserName.toString()
        if (model.server)
            username = model.serverUsername

        for (i in 1..top5Points.size) {
            var map = top5Points["Top${i}"]
            if (map != null) {
                Log.i("teste",model.points.toString() + " " + map.toString())
                if(map.get("points")!!.toInt() < model.points){
                    var mapAux = HashMap<String?,String?>()
                    mapAux.put("points",model.points.toString())
                    mapAux.put("username",username)
                    mapFinal.put("Top${i}",mapAux)
                    for (x in j .. top5Points.size){
                        var map = top5Points["Top${x}"]
                        if (map != null) {
                            mapFinal.put("Top${x+1}",map)
                        }
                    }
                    keepGoing = false
                }
            }
            j++
            if (!keepGoing)
                break
            if (map != null) {
                mapFinal.put("Top${i}",map)
            }
        }
        if(keepGoing && top5Points.size < 5) {
            var mapAux = HashMap<String?,String?>()
            mapAux.put("points",model.points.toString())
            mapAux.put("username",username)
            mapFinal.put("Top${top5Points.size}",mapAux)
        }

        Log.i("teste",mapFinal.toString())

        for (i in 1..top5Points.size)
            updateDataInFirestoreTrans(CollectionPath.MultiplayerPointScores,i, mapFinal.get("Top${i}")!!)
        if(mapFinal.size > top5Points.size && mapFinal.size <= 5) {
            for (i in top5Points.size..mapFinal.size) {
                addDataToFirestore(CollectionPath.MultiplayerPointScores, i, mapFinal.get("Top${i}")!!)
            }
        }

        /////////////////////////////Time///////////////////////////

        mapFinal = HashMap<String,HashMap<String?,String?>>()
        j = 1
        keepGoing = true

        for (i in 1..top5Time.size) {
            var map = top5Time["Top${i}"]
            if (map != null) {
                if(map.get("time")!!.toInt() <= model.timeFirebase){
                    var mapAux = HashMap<String?,String?>()
                    mapAux.put("time",model.timeFirebase.toString())
                    mapAux.put("username",username)
                    mapFinal.put("Top${i}",mapAux)
                    for (x in j .. top5Time.size){
                        var map = top5Time["Top${x}"]
                        if (map != null) {
                            mapFinal.put("Top${x+1}",map)
                        }
                    }
                    keepGoing = false
                }
            }
            j++
            if (!keepGoing)
                break
            if (map != null) {
                mapFinal.put("Top${i}",map)
            }
        }
        if(keepGoing && top5Time.size < 5) {
            var mapAux = HashMap<String?,String?>()
            mapAux.put("time",model.timeFirebase.toString())
            mapAux.put("username",username)
            mapFinal.put("Top${top5Time.size}",mapAux)
        }

        Log.i("teste",mapFinal.toString())

        for (i in 1..top5Time.size)
            updateDataInFirestoreTrans(CollectionPath.MultiplayerTimeScores,i, mapFinal.get("Top${i}")!!)
        if(mapFinal.size > top5Time.size && mapFinal.size <= 5) {
            for (i in top5Time.size..mapFinal.size) {
                addDataToFirestore(CollectionPath.MultiplayerTimeScores, i, mapFinal.get("Top${i}")!!)
            }
        }


    }



    fun gameOver(){
        if(model.isMultiplayerGame) {
            model.gameOverMultiplayer()
            continueThread = false
            updateFirebaseMultiplayer()
            finish()
            gameOver = true
            return
        }

        Log.i("firebase","Antes -> " + top5.toString())
        var mapFinal = HashMap<String,HashMap<String?,String?>>()
        var j = 1
        var keepGoing = true
        for (i in 1..top5.size) {
            var map = top5["Top${i}"]
            if (map != null) {
                if(map.get("time")!!.toInt() <= model.timeFirebase && map.get("points")!!.toInt() < model.points){
                    var mapAux = HashMap<String?,String?>()
                    mapAux.put("time",model.timeFirebase.toString())
                    mapAux.put("points",model.points.toString())
                    mapAux.put("username",model.UserName)
                    mapFinal.put("Top${i}",mapAux)
                    for (x in j .. top5.size){
                        var map = top5["Top${x}"]
                        if (map != null) {
                            mapFinal.put("Top${x+1}",map)
                        }
                    }
                    keepGoing = false
                }
            }
            j++
            if (!keepGoing)
                break
            if (map != null) {
                mapFinal.put("Top${i}",map)
            }
        }
        if(keepGoing && top5.size < 5) {
            var mapAux = HashMap<String?,String?>()
            mapAux.put("time",model.timeFirebase.toString())
            mapAux.put("points",model.points.toString())
            mapAux.put("username",model.UserName)
            mapFinal.put("Top${top5.size}",mapAux)
        }

        Log.i("teste",mapFinal.toString())

        for (i in 1..top5.size)
            updateDataInFirestoreTrans(CollectionPath.SoloScores,i, mapFinal.get("Top${i}")!!)
        if(mapFinal.size > top5.size && mapFinal.size <= 5)
            for(i in top5.size .. mapFinal.size){
                addDataToFirestore(CollectionPath.SoloScores,i, mapFinal.get("Top${i}")!!)
            }

        //Alert Dialag quando acaba o jogo
        alertDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.game_over_dialog))
            .setMessage(getString(R.string.game_over_points_dialog) +" ${model.points}")
            .setPositiveButton(getString(R.string.new_game)){ _: DialogInterface, _: Int ->
                model.createViewModel(this, binding, this)
                model.startGame()
                changeTime()
            }
            .setNegativeButton(getString(R.string.exit)) { _: DialogInterface, _: Int ->
                finish()
            }
            .setCancelable(true)
            .create()

        alertDialog?.show()
    }

    val changeTime = Executors.newSingleThreadExecutor()
    fun changeTime() {
        var secondsTV = binding.seconds
        var time = model.time
        threadTime = thread {
            changeTime.execute {
                while (time > 0 && continueThread){
                    time = model.time
                    secondsTV.post { secondsTV.text = " $time " + getString(R.string.time) }
                    Thread.sleep(1000)
                    if(!model.stopGame) {
                        model.time--
                        if (time == 0) {
                            this.runOnUiThread(Runnable {
                                gameOver()
                            })
                        }
                    }
                }
            }
        }
    }


    private fun startAsServer() {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = wifiManager.connectionInfo.ipAddress
        val strIPAddress = String.format("%d.%d.%d.%d",
            ip and 0xff,
            (ip shr 8) and 0xff,
            (ip shr 16) and 0xff,
            (ip shr 24) and 0xff
        )
        val ll = LinearLayout(this).apply {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            this.setPadding(50, 50, 50, 50)
            layoutParams = params
            orientation = LinearLayout.HORIZONTAL
            addView(ProgressBar(context).apply {
                isIndeterminate = true
                val paramsPB = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                paramsPB.gravity = Gravity.CENTER_VERTICAL
                layoutParams = paramsPB
                indeterminateTintList = ColorStateList.valueOf(Color.RED)
            })
            addView(TextView(context).apply {
                val paramsTV = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams = paramsTV
                text = String.format(getString(R.string.server_ip) + " %s " + getString(R.string.server_wait_client),strIPAddress)
                textSize = 20f
                setTextColor(Color.BLACK)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            })
        }

        dlg = AlertDialog.Builder(this)
            .setTitle(getString(R.string.server_mode) )
            .setView(ll)
            .setOnCancelListener {
                model.stopServer()
                finish()
            }
            .create()

        model.startServer()

        dlg?.show()
    }

    private fun startAsClient() {
        val edtBox = EditText(this).apply {
            maxLines = 1
            filters = arrayOf(object : InputFilter {
                override fun filter(
                    source: CharSequence?,
                    start: Int,
                    end: Int,
                    dest: Spanned?,
                    dstart: Int,
                    dend: Int,
                ): CharSequence? {
                    source?.run {
                        var ret = ""
                        forEach {
                            if (it.isDigit() || it == '.')
                                ret += it
                        }
                        return ret
                    }
                    return null
                }

            })
        }
        val dlg = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.client_mode) )
            .setMessage(getString(R.string.server_ip))
            .setPositiveButton(getString(R.string.connect)) { _: DialogInterface, _: Int ->
                val strIP = edtBox.text.toString()
                if (strIP.isEmpty() || !Patterns.IP_ADDRESS.matcher(strIP).matches()) {
                    Toast.makeText(this@GameActivity, "Address not recognized", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    model.startClient(strIP)
                }
            }
            .setNeutralButton(getString(R.string.connect_emulator)) { _: DialogInterface, _: Int ->
                //model.startClient("10.0.2.2", SERVER_PORT-1)
                // Configure port redirect on the Server Emulator:
                // telnet localhost <5554|5556|5558|...>
                // auth <key>
                // redir add tcp:9998:9999
            }
            .setNegativeButton(getString(R.string.cancel)) { _: DialogInterface, _: Int ->
                finish()
            }
            .setCancelable(false)
            .setView(edtBox)
            .create()

        dlg.show()
    }

    fun dismissServerDialog() {
       if(dlg?.isShowing == true) {
           dlg?.dismiss()
           dlg = null
       }else if(alertDialog?.isShowing == true){
            alertDialog?.dismiss()
            alertDialog = null
        }
    }


    fun updateUI() {
        if(model.state.value == GameViewModel.State.UPDATE_UI && model.numberExpressions != 0) {
            model.initializeBoard()
            model.tvPoints.text = " " + model.points + " " + getString(R.string.points)
            model.tvExpresions.text = " " + model.numberExpressions + " " + getString(R.string.expressions_missing)
            model.tvLevel.text = model.level.toString()
            continueThread = true
            changeTime()
        }else if(model.numberExpressions == 0 && dlg == null) {
            model.tvPoints.text = " " + model.points + " " + getString(R.string.points)
            model.tvExpresions.text = " " + model.numberExpressions + " " + getString(R.string.expressions_missing)
            model.tvLevel.text = model.level.toString()
            continueThread = false
            waitForNewLevel()
        }
        Log.i("clientserverFinish",model.clientFinish.toString() +" - "+ model.serverFinish.toString() +" - "+ ((model.clientFinish && model.serverFinish).toString()))
        if(model.clientFinish && model.serverFinish){
            model.tvPoints.text = " " + model.points + " " + getString(R.string.points)
            model.tvExpresions.text = " " + model.numberExpressions + " " + getString(R.string.expressions_missing)
            model.tvLevel.text = model.level.toString()
            if(model.server) {
                model.newLevelMultiplayer()
            }
        }
        binding.profilePoints1.text = "${model.pointsClient} ${getString(R.string.points)}"

    }

    fun waitForNewLevel() {
            val ll = LinearLayout(this).apply {
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                this.setPadding(50, 50, 50, 50)
                layoutParams = params
                orientation = LinearLayout.HORIZONTAL
                addView(ProgressBar(context).apply {
                    isIndeterminate = true
                    val paramsPB = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                    paramsPB.gravity = Gravity.CENTER_VERTICAL
                    layoutParams = paramsPB
                    indeterminateTintList = ColorStateList.valueOf(Color.RED)
                })
                addView(TextView(context).apply {
                    val paramsTV = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                    layoutParams = paramsTV
                    text = String.format(getString(R.string.wait_players_dialog))
                    textSize = 20f
                    setTextColor(Color.BLACK)
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                })
            }
            dlg = AlertDialog.Builder(this)
                .setTitle(getString(R.string.new_level))
                .setView(ll)
                .setCancelable(false)
                .setOnCancelListener {
                    finish()
                }
                .create()

            dlg?.show()
        }

}


