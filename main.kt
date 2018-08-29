import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

fun main(args: Array<String>) {

    // arg[0]: isFragment 0:false else 1
    // arg[1]: package name
    // arg[2]: file name
    //args[3]: layout name

    var isFragment = args[0]
    var strPack = args[1]
    var strFile = args[2]
    var layoutName = args[3]

    var strWithout_ = strFile.replace("_", "")
    var packageName = strPack + "." + strWithout_

    val words = strFile.split("_").toMutableList()
    var output = ""
    for (word in words) {
        output += word.capitalize() + " "
    }
    output = output.trim()
    var fileName = output.replace(" ", "")


    var currentPath = createPackage(strWithout_)
    createInteractor(fileName, packageName, currentPath)
    createPresenter(fileName, packageName, currentPath)
    createView(fileName, packageName, currentPath, layoutName, isFragment)
    createModule(fileName, packageName, currentPath, isFragment)

    if (isFragment != "0")
        createFramentProv(fileName, packageName, currentPath)

}

fun createPackage(fileName: String): String {
    var  os = System.getProperty("os.name")
    val currentPath = System.getProperty("user.dir")
    val pathName= if(os.contains("windows"))
        currentPath + "\\" + fileName
    else
        currentPath + "/" + fileName

    val wallpaperDirectory = File(pathName)
    // have the object build the directory structure, if needed.
    wallpaperDirectory.mkdirs()
    return pathName
}

fun createInteractor(requiredFileName: String, packName: String, currentPath: String) {
    var usedPackageName = packName + ".interactor";

    var basePack = packName.substring(0, packName.indexOf(".ui"))


    var filename = requiredFileName + "InteractorImpl.kt"

    var file2name = "I" + requiredFileName + "Interactor.kt"
    // create a File object for the parent directory
    val pathName = currentPath + "/interactor"
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
        out.println("package " + usedPackageName)
        out.println("\nimport " + basePack + ".data.network.ApiHelper")
        out.println("import " + basePack + ".data.preferences.PreferenceHelper")
        out.println("import " + basePack + ".ui.base.interactor.BaseInteractorImpl")
        out.println("import javax.inject.Inject\n")

        out.println("class " + requiredFileName + "InteractorImpl " + "@Inject internal constructor(preferenceHelper: PreferenceHelper, apiHelper: ApiHelper) : BaseInteractorImpl(preferenceHelper = preferenceHelper, apiHelper = apiHelper), I" + requiredFileName + "Interactor {\n}")
    }

    output2File.printWriter().use { out ->
        out.println("package " + usedPackageName)
        out.println("\nimport " + basePack + ".ui.base.interactor.IInteractor\n")

        out.println("interface " + "I" + requiredFileName + "Interactor " + ": IInteractor {\n}")
    }
}

fun createPresenter(requiredFileName: String, packName: String, currentPath: String) {
    var basePack = packName.substring(0, packName.indexOf(".ui"))
    var usedPackageName = packName + ".presenter";

    var filename = requiredFileName + "PresenterImpl.kt"

    var file2name = "I" + requiredFileName + "Presenter.kt"
    // create a File object for the parent directory
    val pathName = currentPath + "/presenter"
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
        out.println("package " + usedPackageName)
        out.println("\nimport " + basePack + ".ui.base.presenter.BasePresenterImpl")
        out.println("import " + packName + ".interactor.I" + requiredFileName + "Interactor")
        out.println("import " + packName + ".view.I" + requiredFileName + "View")
        out.println("import " + basePack + ".util.SchedulerProvider")
        out.println("import io.reactivex.disposables.CompositeDisposable")
        out.println("import javax.inject.Inject\n")

        out.println("class " + requiredFileName + "PresenterImpl<V : I" + requiredFileName + "View, I : I" + requiredFileName + "Interactor> @Inject internal constructor(interactor: I, schedulerProvider: SchedulerProvider, disposable: CompositeDisposable) : BasePresenterImpl<V, I>(interactor = interactor, schedulerProvider = schedulerProvider, compositeDisposable = disposable), I" + requiredFileName + "Presenter<V, I> {" +
                "\n"
                + "}")
    }

    output2File.printWriter().use { out ->
        out.println("package " + usedPackageName)
        out.println("\nimport " + basePack + ".ui.base.presenter.IPresenter")
        out.println("import " + packName + ".interactor.I" + requiredFileName + "Interactor")
        out.println("import " + packName + ".view.I" + requiredFileName + "View\n")

        out.println("interface I" + requiredFileName + "Presenter<V : I" + requiredFileName + "View, I : I" + requiredFileName + "Interactor> : IPresenter<V, I> {\n" +
                "}")

    }

}

fun createView(requiredFileName: String, packName: String, currentPath: String, layoutName: String, isFragment: String) {
    var basePack = packName.substring(0, packName.indexOf(".ui"))
    var usedPackageName = packName + ".view";

    var filename = if (isFragment != "0")
        requiredFileName + "Fragment.kt"
    else
        requiredFileName + "Activity.kt"

    var file2name = "I" + requiredFileName + "View.kt"

    val pathName = currentPath + "/view"
    val wallpaperDirectory = File(pathName)
    wallpaperDirectory.mkdirs()

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

    if (isFragment == "0")
        outputFile.printWriter().use { out ->
            out.println("package " + usedPackageName)
            out.println("\nimport " + basePack + ".R")
            out.println("import " + basePack + ".ui.base.view.BaseActivity")
            out.println("import " + packName + ".interactor.I" + requiredFileName + "Interactor")
            out.println("import " + packName + ".presenter.I" + requiredFileName + "Presenter")
            out.println("import " + basePack + ".util.extension.removeFragment")

            out.println("import android.os.Bundle\n" +
                    "import android.support.v4.app.Fragment\n" +
                    "import dagger.android.AndroidInjector\n" +
                    "import dagger.android.DispatchingAndroidInjector\n" +
                    "import dagger.android.support.HasSupportFragmentInjector\n" +
                    "import javax.inject.Inject\n")
            out.println("class " + requiredFileName + "Activity : BaseActivity(), I" + requiredFileName + "View, HasSupportFragmentInjector {\n" +
                    "\n" +
                    "    @Inject\n" +
                    "    internal lateinit var presenter: I" + requiredFileName + "Presenter<I" + requiredFileName + "View, I" + requiredFileName + "Interactor>\n" +
                    "    @Inject\n" +
                    "    internal lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>" +
                    "    \n\n" +
                    "    override fun onCreate(savedInstanceState: Bundle?) {\n" +
                    "        super.onCreate(savedInstanceState)\n" +
                    "        setContentView(R.layout." + layoutName + ")\n" +
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
    else
        outputFile.printWriter().use { out ->
            out.println("package " + usedPackageName)
            out.println("\nimport " + basePack + ".R")
            out.println("import " + basePack + ".ui.base.view.BaseFragment")
            out.println("import " + packName + ".interactor.I" + requiredFileName + "Interactor")
            out.println("import " + packName + ".presenter.I" + requiredFileName + "Presenter")

            out.println("import android.os.Bundle\n" +
                    "import android.view.LayoutInflater\n" +
                    "import android.view.View\n" +
                    "import android.view.ViewGroup\n" +
                    "import javax.inject.Inject\n")

            out.println("class " + requiredFileName + "Fragment : BaseFragment(), I" + requiredFileName + "View {\n" +
                    "\n" +
                    "    @Inject\n" +
                    "    internal lateinit var presenter: I" + requiredFileName + "Presenter<I" + requiredFileName + "View, I" + requiredFileName + "Interactor>\n" +
                    "    companion object {\n" +
                    "        fun newInstance(): " + requiredFileName + "Fragment {\n" +
                    "            val args: Bundle = Bundle()\n" +
                    "            val fragment = " + requiredFileName + "Fragment()\n" +
                    "            fragment.arguments = args\n" +
                    "            return fragment\n" +
                    "        }\n" +
                    "    }\n" +
                    " override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,\n" +
                    "                              savedInstanceState: Bundle?): View? {\n" +
                    "        // Inflate the layout for this fragment\n" +
                    "        val view = inflater.inflate(R.layout." + layoutName + ", container, false)\n" +
                    "        return view\n" +
                    "    }\n" +
                    "\n" +
                    "    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {\n" +
                    "        presenter.onAttach(this)\n" +
                    "        super.onViewCreated(view, savedInstanceState)\n" +
                    "    }\n" +
                    "\n" +
                    "    override fun onDestroy() {\n" +
                    "        presenter.onDetach()\n" +
                    "        super.onDestroy()\n" +
                    "    }\n" +
                    "\n" +
                    "    override fun setUp() {\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "}\n")
        }

    output2File.printWriter().use { out ->
        out.println("package " + usedPackageName)
        out.println("\nimport ae.adnoc.gov.ui.base.view.IView\n")
        out.println("interface " + "I" + requiredFileName + "View : IView {\n" +
                "}")
    }


}

fun createModule(requiredFileName: String, packName: String, currentPath: String, isFragment: String) {

    var filename = if (isFragment != "0")
        requiredFileName + "FragmentModule.kt"
    else
        requiredFileName + "ActivityModule.kt"

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
        out.println("package " + packName)
        out.println("\nimport " + packName + ".interactor." + requiredFileName + "InteractorImpl")
        out.println("import " + packName + ".interactor." + "I" + requiredFileName + "Interactor")
        out.println("import " + packName + ".presenter." + requiredFileName + "PresenterImpl")
        out.println("import " + packName + ".presenter." + "I" + requiredFileName + "Presenter")
        out.println("import " + packName + ".view." + "I" + requiredFileName + "View")
        out.println("import dagger.Module\n" +
                "import dagger.Provides\n")

        if (isFragment == "0")
            out.println("@Module\n" +
                    "class " + requiredFileName + "ActivityModule {\n" +
                    "\n" +
                    "    @Provides\n" +
                    "    internal fun provide" + requiredFileName + "Interactor(" + requiredFileName + "InteractorImpl: " + requiredFileName + "InteractorImpl): I" + requiredFileName + "Interactor = " + requiredFileName + "InteractorImpl" +
                    "\n\n" +
                    "    @Provides\n" +
                    "    internal fun provide" + requiredFileName + "Presenter(" + requiredFileName + "PresenterImpl: " + requiredFileName + "PresenterImpl<I" + requiredFileName + "View, I" + requiredFileName + "Interactor>)\n" +
                    "            : I" + requiredFileName + "Presenter<I" + requiredFileName + "View, I" + requiredFileName + "Interactor> = " + requiredFileName + "PresenterImpl" +
                    "\n" +
                    "}")
        else out.println("@Module\n" +
                "class " + requiredFileName + "FragmentModule {\n" +
                "\n" +
                "    @Provides\n" +
                "    internal fun provide" + requiredFileName + "Interactor(" + requiredFileName + "InteractorImpl: " + requiredFileName + "InteractorImpl): I" + requiredFileName + "Interactor = " + requiredFileName + "InteractorImpl" +
                "\n\n" +
                "    @Provides\n" +
                "    internal fun provide" + requiredFileName + "Presenter(" + requiredFileName + "PresenterImpl: " + requiredFileName + "PresenterImpl<I" + requiredFileName + "View, I" + requiredFileName + "Interactor>)\n" +
                "            : I" + requiredFileName + "Presenter<I" + requiredFileName + "View, I" + requiredFileName + "Interactor> = " + requiredFileName + "PresenterImpl" +
                "\n" +
                "}")
    }

}

fun createFramentProv(requiredFileName: String, packName: String, currentPath: String) {

    var filename = requiredFileName + "FragmentProvider.kt"

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
        out.println("package " + packName)

        out.println("\nimport " + packName + ".view." + requiredFileName + "Fragment")
        out.println("import dagger.Module")
        out.println("import dagger.android.ContributesAndroidInjector\n")
        out.println("@Module\n" +
                "internal abstract class " + requiredFileName + "FragmentProvider {\n" +
                "\n" +
                "    @ContributesAndroidInjector(modules = [" + requiredFileName + "FragmentModule::class])\n" +
                "    internal abstract fun provide" + requiredFileName + "FragmentFactory(): " + requiredFileName + "Fragment\n" +
                "}")
    }

}




