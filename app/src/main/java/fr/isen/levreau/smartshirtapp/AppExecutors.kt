package fr.isen.levreau.smartshirtapp

import android.os.Handler
import android.os.Looper
import android.os.Parcel
import android.os.Parcelable
import java.util.concurrent.Executor
import java.util.concurrent.Executors

open class AppExecutors(
    private val diskIO: Executor,
    private val networkIO: Executor,
    private val mainThread: Executor
) : Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("diskIO"),
        TODO("networkIO"),
        TODO("mainThread")
    ) {
    }

    constructor() : this(
        Executors.newSingleThreadExecutor(),
        Executors.newFixedThreadPool(3),
        MainThreadExecutor()
    )

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AppExecutors> {
        override fun createFromParcel(parcel: Parcel): AppExecutors {
            return AppExecutors(parcel)
        }

        override fun newArray(size: Int): Array<AppExecutors?> {
            return arrayOfNulls(size)
        }
    }
}