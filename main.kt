import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

fun main(args: Array<String>) {
    // arg[0]: isFragment 0:false else 1
    // arg[1]: package name
    // arg[2]: file name
    //args[3]: layout name
    //args[4]: fragment name

    createInteractor(args[2], args[1])
    createPresenter(args[2], args[1])
    createView(args[2], args[1], args[3])
    createActivityModule(args[2], args[1])
    if (args[0]!="0")
        createFramentProv(args[4],args[1] )
}

fun createInteractor(requiredName:String, packName:String) {
    var usedPackageName = packName+".interactor";
    var basePack= packName.substring(0,packName.indexOf(".ui"))


    var filename = requiredName+"InteractorImpl.kt"

    var file2name = "I"+requiredName+"Interactor.kt"
    // create a File object for the parent directory
    val currentPath = System.getProperty("user.dir")
    val pathName= currentPath+"/Interactor"
    val wallpaperDirectory = File(pathName)
    // have the object build the directory structure, if needed.
    wallpaperDirectory.mkdirs()
    // create a File object for the output file
    val outputFile = File(wallpaperDirectory, filename)
    val output2File = File(wallpaperDirectory, file2name)

    // now attach the OutputStream to the file object, instead of a String representation
    try {
        FileOutputStream(outputFile)
        FileOutputStream(output2File)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }

    //fill the files

    outputFile.printWriter().use { out ->
        out.println("package "+usedPackageName)
        out.println("\nimport "+basePack+".data.network.ApiHelper")
        out.println("import "+basePack+".data.preferences.PreferenceHelper")
        out.println("import "+basePack+".ui.base.interactor.BaseInteractorImpl")
        out.println("import javax.inject.Inject\n")

        out.println("class "+requiredName+"InteractorImpl "+ "@Inject internal constructor(preferenceHelper: PreferenceHelper, apiHelper: ApiHelper) : BaseInteractorImpl(preferenceHelper = preferenceHelper, apiHelper = apiHelper), IGasolineInteractor {\n}")
    }

    output2File.printWriter().use { out ->
        out.println("package "+usedPackageName)
        out.println("\nimport "+basePack+".ui.base.interactor.IInteractor\n")

        out.println("interface "+"I"+requiredName+"Interactor "+ ": IInteractor {\n}")
    }
}
fun createPresenter(requiredName:String, packName:String) {
    var basePack= packName.substring(0,packName.indexOf(".ui"))
    var usedPackageName = packName+".presenter";

    var filename = requiredName+"PresenterImpl.kt"

    var file2name = "I"+requiredName+"Presenter.kt"
    // create a File object for the parent directory
    val currentPath = System.getProperty("user.dir")

    val pathName= currentPath+"/presenter"
    val wallpaperDirectory = File(pathName)
    // have the object build the directory structure, if needed.
    wallpaperDirectory.mkdirs()
    // create a File object for the output file
    val outputFile = File(wallpaperDirectory, filename)
    val output2File = File(wallpaperDirectory, file2name)

    // now attach the OutputStream to the file object, instead of a String representation
    try {
        FileOutputStream(outputFile)
        FileOutputStream(output2File)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }

    //fill the files

    outputFile.printWriter().use { out ->
        out.println("package "+usedPackageName)
        out.println("\nimport "+basePack+".ui.base.presenter.BasePresenterImpl")
        out.println("import "+packName+".interactor.I"+requiredName+"Interactor")
        out.println("import "+packName+".view.I"+requiredName+"View")
        out.println("import "+basePack+".util.SchedulerProvider")
        out.println("import io.reactivex.disposables.CompositeDisposable")
        out.println("import javax.inject.Inject\n")

        out.println("class "+requiredName+"PresenterImpl<V : I"+ requiredName+"View, I : I"+requiredName+"Interactor> @Inject internal constructor(interactor: I, schedulerProvider: SchedulerProvider, disposable: CompositeDisposable) : BasePresenterImpl<V, I>(interactor = interactor, schedulerProvider = schedulerProvider, compositeDisposable = disposable), I"+requiredName+"Presenter<V, I> {"+
                "\n"
                +"}")
    }

    output2File.printWriter().use { out ->
        out.println("package "+usedPackageName)
        out.println("\nimport "+basePack+".ui.base.presenter.IPresenter")
        out.println("import "+packName+".interactor.I"+requiredName+"Interactor")
        out.println("import "+packName+".view.I"+requiredName+"View\n")

        out.println("interface I"+requiredName+"Presenter<V : I"+requiredName+"View, I : I"+requiredName+"Interactor> : IPresenter<V, I> {\n" +
                "}")

    }

}
fun createView(requiredName:String, packName:String, layoutName:String) {
    var basePack= packName.substring(0,packName.indexOf(".ui"))
    var usedPackageName = packName+".view";

    var filename = requiredName+"Activity.kt"

    var file2name = "I"+requiredName+"View.kt"
    // create a File object for the parent directory
    val currentPath = System.getProperty("user.dir")

    val pathName= currentPath+"/view"
    val wallpaperDirectory = File(pathName)
    // have the object build the directory structure, if needed.
    wallpaperDirectory.mkdirs()
    // create a File object for the output file
    val outputFile = File(wallpaperDirectory, filename)
    val output2File = File(wallpaperDirectory, file2name)

    // now attach the OutputStream to the file object, instead of a String representation
    try {
        FileOutputStream(outputFile)
        FileOutputStream(output2File)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }

    //fill the files

    outputFile.printWriter().use { out ->
        out.println("package "+usedPackageName)
        out.println("\nimport "+basePack+".R")
        out.println("import "+basePack+".ui.base.view.BaseActivity")
        out.println("import "+packName+".interactor.I"+requiredName+"Interactor")
        out.println("import "+packName+".presenter.I"+requiredName+"Presenter")
        out.println("import "+basePack+".util.extension.removeFragment")

        out.println("import android.os.Bundle\n" +
                "import android.support.v4.app.Fragment\n" +
                "import dagger.android.AndroidInjector\n" +
                "import dagger.android.DispatchingAndroidInjector\n" +
                "import dagger.android.support.HasSupportFragmentInjector\n" +
                "import javax.inject.Inject\n")
        out.println("class "+requiredName+"Activity : BaseActivity(), I"+requiredName+"GasolineView, HasSupportFragmentInjector {\n" +
                "\n" +
                "    @Inject\n" +
                "    internal lateinit var presenter: I"+requiredName+"Presenter<I"+requiredName+"View, I"+requiredName+"Interactor>\n" +
                "    @Inject\n" +
                "    internal lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>" +
                "    \n\n" +
                "    override fun onCreate(savedInstanceState: Bundle?) {\n" +
                "        super.onCreate(savedInstanceState)\n" +
                "        setContentView(R.layout."+layoutName+")\n" +
                "    }\n\n" +
                "    override fun onFragmentAttached(tag: String?) {\n" +
                "    }\n\n" +
                "    override fun onFragmentDetached(tag: String) {\n" +
                "        supportFragmentManager.removeFragment(tag)\n" +
                "    }\n" +
                "\n" +
                "    override fun onDestroy() {\n" +
                "        presenter.onDetach()\n" +
                "        super.onDestroy()\n" +
                "    }\n" +
                "\n" +
                "    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector\n" +
                "\n" +
                "}\n")
    }
    output2File.printWriter().use { out ->
        out.println("package "+usedPackageName)
        out.println("\nimport ae.adnoc.gov.ui.base.view.IView\n")
        out.println("interface "+"I"+requiredName+"View : IView {\n" +
                "}")
    }

}

fun createActivityModule(requiredName:String, packName:String){

    var filename = requiredName+"ActivityModule.kt"

    val currentPath = System.getProperty("user.dir")
    val wallpaperDirectory = File(currentPath)
    wallpaperDirectory.mkdirs()
    val outputFile = File(wallpaperDirectory, filename)

    try {
        FileOutputStream(outputFile)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }

    //fill the files

    outputFile.printWriter().use { out ->
        out.println("package "+packName)
        out.println("\nimport "+packName+".interactor."+requiredName+"InteractorImpl")
        out.println("import "+packName+".interactor."+"I"+requiredName+"Interactor")
        out.println("import "+packName+".presenter."+requiredName+"PresenterImpl")
        out.println("import "+packName+".presenter."+"I"+requiredName+"Presenter")
        out.println("import "+packName+".view."+"I"+requiredName+"View")
        out.println("import dagger.Module\n" +
                "import dagger.Provides\n")


        out.println("@Module\n" +
                "class "+requiredName+ "ActivityModule {\n" +
                "\n" +
                "    @Provides\n" +
                "    internal fun provide"+requiredName+"Interactor("+requiredName+"InteractorImpl: "+requiredName+"InteractorImpl): I"+requiredName+"Interactor = "+requiredName+"InteractorImpl" +
                "\n" +
                "    @Provides\n" +
                "    internal fun provide"+requiredName+"Presenter("+requiredName+"PresenterImpl: "+requiredName+"PresenterImpl<I"+requiredName+"GasolineView, I"+requiredName+"Interactor>)\n" +
                "            : I"+requiredName+"Presenter<I"+requiredName+"View, I"+requiredName+"Interactor> = "+requiredName+"PresenterImpl" +
                "\n" +
                "}")
    }

}

fun createFramentProv(requiredName: String, packName: String){

    var filename = requiredName+"FragmentProvider.kt"

    // create a File object for the parent directory
    val currentPath = System.getProperty("user.dir")
    val wallpaperDirectory = File(currentPath)
    // have the object build the directory structure, if needed.
    wallpaperDirectory.mkdirs()
    // create a File object for the output file
    val outputFile = File(wallpaperDirectory, filename)

    // now attach the OutputStream to the file object, instead of a String representation
    try {
        FileOutputStream(outputFile)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }

    //fill the files

    outputFile.printWriter().use { out ->
        out.println("package " + packName)

        out.println("\nimport "+packName+".view."+requiredName+"Fragment")
        out.println("import dagger.Module")
        out.println("import dagger.android.ContributesAndroidInjector\n")
        out.println("@Module\n" +
                "internal abstract class " + requiredName + "FragmentProvider {\n" +
                "\n" +
                "    @ContributesAndroidInjector(modules = [" + requiredName + "FragmentModule::class])\n" +
                "    internal abstract fun provide" + requiredName + "FragmentFactory(): " + requiredName + "Fragment\n" +
                "}")
    }

}




