/*
* Copyright 2010 Research In Motion Limited.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
/*
 rapc.exe options:

    -dumpcodfile <input>.cod [<output>.cod]
    -dumpsigner [-inherit] <input>.cod
    -dumpdebug <input>.debug
    -dumpclassfile <input>.class
    -dumpdepend <input>{.class|.jar}
    -eviscerateclass {<input>.class|<directory>}
    -verifydebug <input>.cod

    Note that the contents of a RAPC environment variable are added by rapc.exe
    to the command line used to invoke rapc.jar
        i.e. set RAPC_OPTIONS=-timing

rapc.jar options:

    <input>.java
    <input>.class
    <input>.jar
        input files of java classes to include in the output codfile
    <input>.jad
        input file for application information. i.e. version, name, description, etc...
    <directory>
        root of a directory tree that should be recursively scanned for .class, .key and resource files
    -codename=[<path>/[...]]<filename>
        specify output codfile name and location
    -library=[<path>/[...]]<filename>
        specify output codfile name and location, also that codfile is a library
    -listing=[<path>/[...]]<file>.lst
        specify output listing file
    -nolisting
        override to stop generation of listing file, disables -listing option
    -debuglisting
        generate listing of debug info into codfile listing
    -debugtest
        read in generated .debug file and regenerate into .debug2
        (these two files should be identical)
    -import=<file>.jar[;...]
        list dependent jar files
    -defs=<file>.def[;...]
        list rapc .def files
    -classpath=<path>[;...]
        list locations of classes that javac will want
    -class=<classname>
        specify name of class containing main entry point
    -jad=<file>jad
        read file for application information. i.e. version, name, url, etc...
    -noimport
        the resulting library may not be linked against
    -midlet
        the resulting codfile is a midlet, generate a preverified .jar file too
    -exepath=<path>
        where to find preverify.exe when building midlets
    -eviscerate
    -noeviscerate
        .class files that are collected into output .jar file have the
        code attributes removed.  all methods are marked as native.
        eviscerate is default when building library, otherwise noeviscerate.
    -convertpng
        when generating image data as a binary resource, do convert the
        image file format to PNG
    -noshortname
        when generating binary resources, only use the long name
    -noparsecod
        when reading import .jar files, don't try to parse any .cod files, only
        read the .class files for import information
    -nodebug
        suppress generation of .debug output file (and debug attributes in .class files)
        has side effect of setting timestamp to fixed value
    -debugclass
        enable generation of debug attributes in .class files, overrides nodebug
    -notarget13
        don't pass "-target 1.3" to javac, "-target 1.3" tells JDK 1.4 javac
        to behave more like JDK 1.3, this works better for us
    -target11
        do pass "-target 1.1" to javac, this is how we used to do things
    -javacompiler=<toolname>
        use <toolname> as the java compiler.  default is javac.
        jikes and wjava have been tested.
    -nolimit
        don't abort compilation if a sibling .cod file gets too big.
    -noloadtool
        when running the JDK javac or jar programs, don't try to load them into
        the current VM, execute them as external processes
    -deprecation
        pass -deprecation switch to java compiler
    -nowarn
        pass -nowarn switch to the java compiler
        also suppress warnings generated by rapc
    -quiet
        only display errors
    -warning
        generate warning messages
    -nopackagewarning
        suppress warning about package name not matching directory structure
    -verbose
        dump information about what is going on, leave temp files lying around
    -traceback
        if an error occurs, dump the java execption trackback
    -timing
        display information about time taken for various phases of compilation
    -exclusive
        try to leave unreferenced components out of library cod files.
        Will break published API.
    -nativesonly
        don't produce codfile output, only XXXnatives.h and XXXnativeType.h output files
    -optimizepackage
        suppress package protection symbolic information.  may reduce codfile size.
        does not allow packages to straddle two cod files.
    -preverified
        use the sun preverifier stackmap information in the generate codfile
    -nooptimize[={nop,arrayinit,deadcode,checkcast,trivial,jump,accessor,mutator,pushpop,useless_case}]
        suppress optimizations
    -define=<label>[;...]
        run javapp style pre-processor with <label> defined
    -sibling
        generate sibling .cod files, this is now the default behaviour
    -target=3.2
        don't generate sibling .cod files, do things the old way
    -target=lastrel
        generate codefiles the previous release can use as well as headrev.
        This option is used ease the pain of developing on headrev and the current release.
        Anything further back get install or sync to the branch.   
    -wx
        treat some warnings as errors
    -nowx=a.b.c.X[;...]
        don't treat warnings as errors for class a.b.c.X
    -snapshot
        when building from .java source files create a <name>-snapshot.jar
        file containing the not preverified and not eviscerated .class files
    -t0
        set timestamp to a fixed value instead of the current seconds
    -warnkey=0xabcdef99[;...]
        generate a warning if specified key needs to be added to the .csl file
    -workspace=<filename>
        adds the <filename> to the .debug output file so the IDE can do better
        browsing
    -tmpdir=<directory>
        use the specified directory for temporary file storage (perhaps a ramdisk)
    -codefull=NNNN
        use NNNN as the limit to how much code goes into a sibling .cod file
        before a new sibling is started, default 63488
    -datafull=NNNN
        use NNNN as the limit to how much data goes into a sibling .cod file
        before a new sibling is started, default 61440
    -vtablefull=NNNN
        use NNNN as the limit to how large the vtable estimate for the
        classes in a sibling .cod file can get before a new sibling is
        started, default 61440
    -fieldfull=NNNN
        use NNNN as the limit to how how many fields a class can add
        to a sibling .cod file before a new sibling is started, default 61440
    -slicesize=NNNN
        use NNNN as the slice size associated with a large binary resource file
        default is 8192, a large binary resource file is larger than 60k
    -languages=<iso-code>[;<iso-code>...]
        mark the .cod file as providing translated language resources for <iso-code>
    -noverifyerr
        set the "IsNoVerifyErr" flag in the codfile to enforce no verify errors when linking.
        Some errors like missing methods are otherwise allowed that will be detected at runtime.
    -iconsize=NNNN
        use NNNN as the maximum allowed size for icon data, default is 16384
    -resourcesize=NNNNN
        use NNNN as the max binary resource size allowed before the file is sliced up
        default is 61440
    -sourceroot=<directory>[;...]
        use list of directories to find .java source file names for debug info
    -emitstatic=<prefix>
        generate a file with all the static final constant values for every
        class whos fullname (including package) starts with prefix
        this is useful for some of the TCK signature tests that assume we
        have such data on the device
    -cr
        compress resource files
    -rtninfo
        generate .wts output files with info for the OTASL upgrade process to
        be able to estimate how much flash will be required by the destination
        bundle for VM data structures
    -noexport
        do not output an export.xml file when generating a library cod file
    -nojar
        do not output a .jar file
    -alias=<name>
        add an alias <name> to a codfile i.e. rapc -codename=<codfile> -alias=<name>

The following options generally appear in the .def files:

    -natives=<method-prototype>[;...]
        native methods
    -exports=<methodprototype>[;...]
        methods called from native code
    -diagnoseexports=<method-prototype>[;...]
        methods that are diagnosed, as a warning, if missing
    -statics=<member-name>[;...]
        static data that is accessed from native code
    -virtual methods=<method-prototype>[;...]
        virtual methods that are called from native code
    -interface methods=<method-prototype>[;...]
        interface methods that are called from native code
    -fields=<member-name>[;...]
        instance data that is accessed from native code
    -strings=<member-name>[;...]
        literal data that is accessed from native code
    -persistable=<interfacename>[;...]
        interfaces that are implicitly persistable, the first
        entry in this list is the marker interface
    -classes=<classname>[;...]
        classes that are accessed from native code
    -interfaces=<interfacename>[;...]
        interfaces that are accessed from native code
    -rootclassvtable=<method-prototype>[;...]
        methods that appear in the vtable for java.lang.Object

    Use one of -codename or -library, if both are specified, -library wins.
    Don't use -eviscerate when making a midlet, or the output .jar file won't be
        a valid midlet.
    For options that have '=', the leading '-' is optional.
    Options may appear on the command line or in the .def file.
    In the .def file the option name is on a single line, enclosed in '[' and ']'.
        the option values appear individually on subsequent lines.
 */

package net.rim.tumbler.rapc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.rim.tumbler.config.WidgetConfig;
import net.rim.tumbler.file.FileManager;
import net.rim.tumbler.session.BBWPProperties;
import net.rim.tumbler.session.SessionManager;

public class Rapc {
    // Output file extensions
    public static String[] EXTENSIONS = new String[] {
        ".cod", ".jar", ".csl", ".cso", ".rapc", ".jar" 
    };
    
    private final String EOL = System.getProperty("line.separator");
    
    // RAPC Collections
    private List<String> _imports = new ArrayList<String>();
    private List<String> _inputFiles = new ArrayList<String>();

    // Rapc single arguments
    // Class name that contains the main entry point
    private String _className = null;
    private String _codeName = null;
    private String _additional = null;
    private String _javac = null;

    // Paths
    private String _cwd;
    private String _bin;

    // Data
    private Map<String, String> _env;
    private WidgetConfig        _widgetConfig;

    // / <summary>
    // / Default constructor
    // /
    // / Constructor uses predetermined defaults
    // / </summary>
    public Rapc(BBWPProperties bbwpProperties, WidgetConfig widgetConfig) {
        _widgetConfig = widgetConfig;
        _bin = bbwpProperties.getRapc();
        _imports = bbwpProperties.getImports();
        _javac = bbwpProperties.getJavac();
        _cwd = SessionManager.getInstance().getSourceFolder();
        _codeName = SessionManager.getInstance().getArchiveName();
        _additional = SessionManager.getInstance().isVerbose() ?
                "-warning" : bbwpProperties.getAdditional() + " -nowarn";
        
        
        // Set system environment variables
        _env = new HashMap<String, String>();
        for (Map.Entry<String, String> val : System.getenv().entrySet()) {
            _env.put(val.getKey().toUpperCase(), val.getValue());
        }
        // A patch to force rapc.exe to search for rapc.jar first in the current
        // directory
        _env.put("PATH", System.getenv("PATH") + ";"
                + System.getProperty("user.dir") + ";" + getJavaBin(bbwpProperties.getJavaHome()));        
    }

    // / <summary>
    // / Generate the parameters to pass to rapc
    // / </summary>
    // / <returns>A String of passable parameters</returns>
    public String GenerateParameters() {
        // forcing the -noshortname switch for rapc
        // http://hhappsweb/hhwiki/Browser/BrowserField2#Display_a_Web_Page_from_a_resource_within_your_COD_file
        String param = "-noshortname ";

        if (_className != null && _className.trim() != "")
            param += "-class \"" + _className + "\" ";

        if (_codeName != null && _codeName.trim() != "")
            param += "-codename=\"" + _codeName + "\" ";

        if (_imports.size() > 0) {
            param += "-import=\"";
            for (int i = 0; i < _imports.size(); ++i) {
                param += _imports.get(i);
                if (i < _imports.size() - 1 && _imports.size() != 1)
                    param += ";";
            }
            param += "\" ";
        }

        if (_javac != null && _javac.trim() != "") {
            param += "-javacompiler=";
            param += _javac;
            param += " ";
        }

        if (_additional != null && _additional.trim() != "")
            param += _additional + " ";
        
             for (String file : _inputFiles)
                    param += "\"" + file + "\" ";

        return param.trim();
    }

    // / <summary>
    // / The input files:
    // / .java: A Java source program file that javac must compile.
    // / .class: A Java .class file that javac has compiled.
    // / .jar: An archive of files that you need to include in the compilation
    // set.
    // / .jad: An input file that contains application information. For example,
    // it contains a list of attributes that the MIDP specification specifies.
    // / </summary>
    public List<String> getInputFiles() {
        return _inputFiles;
    }

    // / <summary>
    // / Specify the name and location of the output .cod file;
    // / typically the output .cod file uses the same name of the .jar file.
    // /
    // / = &lt;path&gt;\[...]]&lt;filename&gt;
    // / </summary>
    public String getCodeName() {
        return this._codeName;
    }

    // / <summary>
    // / The name of the class that contains the main entry point of the
    // application; without this option, RAPC uses the first main(String[])
    // method it finds as the entry point.
    // /
    // / = &lt;classname&gt;
    // / </summary>
    public String getMainEntryPointClassFile() {
        return this._className;
    }

    // / <summary>
    // / -library
    // / Specify the name and location of the output .cod file as a library.
    // /
    // / = [&lt;path&gt;\[...]]&lt;filename&gt;
    // / </summary>

    // / <summary>
    // / -quiet
    // / Display errors only
    // / </summary>

    // / <summary>
    // / -verbose
    // / Display information about RAPC activity. RAPC stores this
    // / information in intermediate and temporary files in the
    // / user's temporary folder. RAPC does not delete the temporary
    // / files.
    // / </summary>

    // / <summary>
    // / List dependent .jar files; for example list RIM APIs and other
    // dependent libraries.
    // /
    // / = &lt;file&gt;.jar[;...]
    // / </summary>
    public List<String> getImports() {
        return this._imports;
    }

    // / <summary>
    // / Any additional non supported rapc arguments to pass
    // / to the executable
    // / </summary>
    public String getAdditionalParameters() {
        return this._additional;
    }

    // / <summary>
    // / Java compiler supported rapc arguments to pass
    // / to the executable
    // / </summary>
    public String getJavac() {
        return this._javac;
    }

    // / <summary>
    // / Returns folder where javac.exe and java.exe files are located
    // / based on entry in bbwp property file.
    // / </summary>
    private String getJavaBin(String javaHome) {
    	if(javaHome != null && javaHome.trim() != "") {
            return javaHome + File.separator + "bin";
    	}
    	
    	return "";
    }
    
    // / <summary>
    // / Runs process with generated parameters
    // / </summary>
    // / <returns>if succeeds: true, otherwise false</returns>
    public boolean run(List<String> inputFiles) throws Exception{
        _inputFiles = inputFiles;
        _inputFiles.add(generateRapcFile());
        return run(GenerateParameters());
    }

    // / <summary>
    // / Runs process with specified parameters
    // / </summary>
    // / <returns>if succeeds: true, otherwise false</returns>
    private boolean run(String parameters) {
        Iterator<Map.Entry<String, String>> iterator = 
            _env.entrySet().iterator();
        List<String> envList = new ArrayList<String>(_env.size());
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            // Doesn't throw an exception.
            // But we want our own variables to take priority over preset ones
            // So remove duplicates
            envList.add(entry.getKey() + "=" + entry.getValue());
        }

        Process proc;
        try {
            File dir = new File(this._cwd);
            proc = Runtime.getRuntime().exec("\"" + _bin + "\" " + parameters,
                    envList.toArray(new String[envList.size()]), dir);
        } catch (IOException ex) {
            System.err.println(ex);
            return false;
        }

        StreamRedirector isr = new StreamRedirector(proc.getInputStream(),
                (OutputStream) System.out);
        StreamRedirector esr = new StreamRedirector(proc.getErrorStream(),
                (OutputStream) System.out);

        isr.start();
        esr.start();

        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            System.out.println("rapc interrupted");
            return false;
        }

        try {
            isr.join();
            esr.join();
        } catch (InterruptedException e) {
            System.out.println("stream redirectors interrupted");
            return false;
        }

        if (proc.exitValue() != 0)
            return false;

        return true;
    }

    // / <summary>
    // / The current working directory for the executables to run in
    // / </summary>
    public void setCurrentWorkingDirectory(String cwd) {
        this._cwd = cwd;
    }

    public String getCurrentWorkingDirectory() {
        return this._cwd;
    }

    // / <summary>
    // / Environment variables to set when executing the Preverify and RAPC
    // binaries
    // / </summary>
    public void setEnvironmentVariables(Map<String, String> env) {
        this._env = env;
    }

    public Map<String, String> getEnvironmentVariables() {
        return this._env;
    }
    
    // Generate our .rapc file
    private String generateRapcFile() throws IOException {
        String fileName = _cwd + File.separator + SessionManager.getInstance().getArchiveName() + ".rapc";
        //BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        //This ensures that the generated RAPC file is UTF8 encoded
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),"UTF8"));
        
        writer.write("MIDlet-Name: " + _widgetConfig.getName() + EOL);
        if (_widgetConfig.getDescription() != null) {
            writer.write("MIDlet-Description: " + _widgetConfig.getDescription() + EOL);
        }
        writer.write("MIDlet-Version: " + _widgetConfig.getVersion() + EOL);
        writer.write("MIDlet-Vendor:");
        if (_widgetConfig.getAuthor() != null && _widgetConfig.getAuthor().length() != 0) {
        	writer.write(" " + _widgetConfig.getAuthor());
        } else {
        	writer.write(" Unknown");
        }
        writer.write(EOL);
        writer.write("MIDlet-Jar-URL: " + SessionManager.getInstance().getArchiveName() + ".jar" + EOL);
        writer.write("MIDlet-Jar-Size: 0" + EOL);
        writer.write("MicroEdition-Profile: MIDP-2.0" + EOL);
        writer.write("MicroEdition-Configuration: CLDC-1.1" + EOL);

        Vector<String> icons = _widgetConfig.getIconSrc();
        String icon = (icons == null || icons.isEmpty()) ? "" : icons.elementAt(0);
        
        writer.write("MIDlet-1: " + _widgetConfig.getName() + "," + icon + ",rim:foreground;WIDGET;");
        if (SessionManager.getInstance().debugMode()) {
        	writer.write("DEBUG_ENABLED");
        }
        writer.write(EOL);
        
        if(_widgetConfig.getBackgroundSource()!=null&&_widgetConfig.isStartupEnabled()) {
        	writer.write("MIDlet-2:,,rim:runOnStartup"+EOL);
        }
        	
        // The rest icons
        int iconCount = 0;
        if (icons != null) {
            for (int i = 1; i < icons.size(); i++) {
                iconCount = iconCount + 1;
                writer.write("RIM-MIDlet-Icon-1-" + iconCount + ": "
                            + icons.elementAt(i) + EOL);
            }
        }

        /// Hover icons are not displayed correctly in widget, if 
        /// used as icons
        /// FIX - make copies of all hover icons
        Vector<String> hoverIcons = _widgetConfig.getHoverIconSrc();
        if (hoverIcons != null) {
            for (int i = 0; i < hoverIcons.size(); i++) {
                iconCount = iconCount + 1;
               	writer.write("RIM-MIDlet-Icon-1-" + iconCount + ": "
                            + copyIcon(
                                    hoverIcons.elementAt(i),
                                    SessionManager.getInstance().getSourceFolder())
                            + ",focused" + EOL);
            }
        }

        if (iconCount > 0) {
            writer.write("RIM-MIDlet-Icon-Count-1: " + iconCount + EOL);
        }
        
        //TODO:CLEAN UP THIS LOGIC ELSEWHERE
        if(_widgetConfig.getForegroundSource().length()==0) {
        	writer.write("RIM-MIDlet-Flags-1: 2" + EOL);
        } else {
        	writer.write("RIM-MIDlet-Flags-1: 0" + EOL);
        }
        
        //Alternate Entry point
        if(_widgetConfig.getBackgroundSource()!=null&&_widgetConfig.isStartupEnabled()) {
        	writer.write("RIM-MIDlet-Flags-2: 3" + EOL);
        }
	
        writer.close();

        return fileName;
    }
    
    private String copyIcon(String icon, String directory) {
        String tempPrefix = "____HOVER_ICON_";
        
        // Copy the file with new prefix
        File from = new File(directory + File.separator + icon);              
        File to = new File(from.getParent() + File.separator + tempPrefix + from.getName());          
        if ((!to.exists()) && from.exists()) {
            try {
                FileManager.copyFile(from, to);
                // Return copied icon file name
                return to.getAbsolutePath().substring(directory.length()+1);
            } catch (Exception e) {
                return icon;
            }
        }
        return icon;
    }
}

class StreamRedirector extends Thread {
    OutputStream os;
    InputStream is;

    public StreamRedirector(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    public void run() {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            System.out.println("error redirecting stream");
        }
    }
}
