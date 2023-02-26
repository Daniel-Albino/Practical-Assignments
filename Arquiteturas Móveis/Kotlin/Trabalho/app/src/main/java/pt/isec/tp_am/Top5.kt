package pt.isec.tp_am

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.tp_am.databinding.ActivityTop5Binding
import kotlin.concurrent.thread

class Top5 : AppCompatActivity() {
    private lateinit var binding : ActivityTop5Binding
    private var listenerRegistration_0 : ListenerRegistration? = null
    private var listenerRegistration_1 : ListenerRegistration? = null
    private var listenerRegistration_2 : ListenerRegistration? = null
    enum class CollectionPath {
        SoloScores, MultiplayerPointScores, MultiplayerTimeScores
    } // private var collectionPath : String = ""
    private var top5 = HashMap<String, HashMap<String?,String?>>()
    private var top5Time = HashMap<String, HashMap<String?,String?>>()
    private var top5Points = HashMap<String, HashMap<String?,String?>>()

    enum class State {
        STARTING, UPDATE_UI
    }
    private val _state = MutableLiveData(State.STARTING)
    val state : LiveData<State>
        get() = _state

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTop5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        startObserverMultiplayerPoints(GameActivity.CollectionPath.MultiplayerPointScores)
        startObserverSinglePlayer(GameActivity.CollectionPath.SoloScores)
        startObserverMultiplayerTime(GameActivity.CollectionPath.MultiplayerTimeScores)


        _state.postValue(State.STARTING)

        state.observe(this) {
            ui()
        }

        thread {
            Thread.sleep(1000)
            Log.i("teste",top5.toString() + " " + top5Points.toString() + " " + top5Time.toString())
            _state.postValue(State.UPDATE_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        stopObserver()
    }

    private fun stopObserver() {
        listenerRegistration_0?.remove()
        listenerRegistration_1?.remove()
        listenerRegistration_2?.remove()
    }

    private fun startObserverSinglePlayer(collectionPath : GameActivity.CollectionPath) {
        val db = Firebase.firestore

        for (i in 1..5) {
            listenerRegistration_0 = db.collection(collectionPath.toString()).document("Top${i}")
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
    private fun startObserverMultiplayerTime(collectionPath : GameActivity.CollectionPath) {
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

    private fun startObserverMultiplayerPoints(collectionPath : GameActivity.CollectionPath) {
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

    fun ui(){

        var s = getString(R.string.no_data_available)


        binding.nameTop1s.text = if(top5["Top1"] == null) s else top5["Top1"]?.get("username").toString()
        binding.timeTop1s.text = if(top5["Top1"] == null) s else top5["Top1"]?.get("time").toString()
        binding.pointsTop1s.text = if(top5["Top1"] == null) s else top5["Top1"]?.get("points").toString()

        binding.nameTop2s.text = if(top5["Top2"] == null) s else top5["Top2"]?.get("username").toString()
        binding.timeTop2s.text = if(top5["Top2"] == null) s else top5["Top2"]?.get("time").toString()
        binding.pointsTop2s.text = if(top5["Top2"] == null) s else top5["Top2"]?.get("points").toString()

        binding.nameTop3s.text = if(top5["Top3"] == null) s else top5["Top3"]?.get("username").toString()
        binding.timeTop3s.text = if(top5["Top3"] == null) s else top5["Top3"]?.get("time").toString()
        binding.pointsTop3s.text = if(top5["Top3"] == null) s else top5["Top3"]?.get("points").toString()

        binding.nameTop4s.text = if(top5["Top4"] == null) s else top5["Top4"]?.get("username").toString()
        binding.timeTop4s.text = if(top5["Top4"] == null) s else top5["Top4"]?.get("time").toString()
        binding.pointsTop4s.text =if(top5["Top4"] == null) s else  top5["Top4"]?.get("points").toString()

        binding.nameTop5s.text = if(top5["Top5"] == null) s else top5["Top5"]?.get("username").toString()
        binding.timeTop5s.text = if(top5["Top5"] == null) s else top5["Top5"]?.get("time").toString()
        binding.pointsTop5s.text = if(top5["Top5"] == null) s else top5["Top5"]?.get("time").toString()

        //////Mutliplayer Points

        binding.nameTop1mp.text = if(top5Points["Top1"] == null) s else top5Points["Top1"]?.get("username").toString()
        binding.pointsTop1mp.text = if(top5Points["Top1"] == null) s else top5Points["Top1"]?.get("points").toString()

        binding.nameTop2mp.text = if(top5Points["Top2"] == null) s else top5Points["Top2"]?.get("username").toString()
        binding.pointsTop2mp.text = if(top5Points["Top2"] == null) s else top5Points["Top2"]?.get("points").toString()

        binding.nameTop3mp.text = if(top5Points["Top3"] == null) s else top5Points["Top3"]?.get("username").toString()
        binding.pointsTop3mp.text = if(top5Points["Top3"] == null) s else top5Points["Top3"]?.get("points").toString()

        binding.nameTop4mp.text = if(top5Points["Top4"] == null) s else top5Points["Top4"]?.get("username").toString()
        binding.pointsTop4mp.text = if(top5Points["Top4"] == null) s else top5Points["Top4"]?.get("points").toString()

        binding.nameTop5mp.text = if(top5Points["Top5"] == null) s else top5Points["Top5"]?.get("username").toString()
        binding.pointsTop5mp.text = if(top5Points["Top5"] == null) s else top5Points["Top5"]?.get("points").toString()


        //////Mutliplayer Time

        binding.nameTop1mt.text = if(top5Time["Top1"] == null) s else top5Time["Top1"]?.get("username").toString()
        binding.timeTop1mt.text = if(top5Time["Top1"] == null) s else top5Time["Top1"]?.get("time").toString()

        binding.nameTop2mt.text = if(top5Time["Top2"] == null) s else top5Time["Top2"]?.get("username").toString()
        binding.timeTop2mt.text = if(top5Time["Top2"] == null) s else top5Time["Top2"]?.get("time").toString()

        binding.nameTop3mt.text = if(top5Time["Top3"] == null) s else top5Time["Top3"]?.get("username").toString()
        binding.timeTop3mt.text = if(top5Time["Top3"] == null) s else top5Time["Top3"]?.get("time").toString()

        binding.nameTop4mt.text = if(top5Time["Top4"] == null) s else top5Time["Top4"]?.get("username").toString()
        binding.timeTop4mt.text = if(top5Time["Top4"] == null) s else top5Time["Top4"]?.get("time").toString()

        binding.nameTop5mt.text = if(top5Time["Top5"] == null) s else top5Time["Top5"]?.get("username").toString()
        binding.timeTop5mt.text = if(top5Time["Top5"] == null) s else top5Time["Top5"]?.get("time").toString()
    }


}