package coveragetool;

import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;
import java.util.Hashtable;
import java.util.List;

public class MyThread extends Thread {

    public MyThread() {
        try {
            jbInit();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    VirtualMachine vm;
    boolean stopOnVMStart;
    boolean connected = true;
    int numClasses;
    String pkgName;
    Hashtable hashTable = new Hashtable(50);
    ReferenceType type;
    CoverageTool ct;
    public MyThread(VirtualMachine vm, boolean stopOnVMStart, String pkgName, int numClasses, CoverageTool ct) {
        this.vm = vm;
        this.stopOnVMStart = stopOnVMStart;
        this.numClasses = numClasses;
        this.pkgName = pkgName;
        this.ct = ct;
        this.start();
    }

    public void run() {
        EventQueue queue = vm.eventQueue();
        for (int i = 1; i <= numClasses; i++) {
            ClassPrepareRequest cpr = vm.eventRequestManager().createClassPrepareRequest();
            cpr.addClassFilter(pkgName + ".*");
            cpr.addCountFilter(i);
            cpr.enable();
        }

        while (connected) {
            try {
                EventSet eventSet = queue.remove();
                boolean resumeStoppedApp = false;
                EventIterator it = eventSet.eventIterator();
                while (it.hasNext()) {
                    Event e = it.nextEvent();
                    resumeStoppedApp |= !handleEvent(e);
                }
                if (resumeStoppedApp) {
                    eventSet.resume();
                }
            } catch (Exception exc) {
                System.out.println(exc);
            }
        }
    }

    private boolean handleEvent(Event event) {
        if (event instanceof ExceptionEvent) {
            return exceptionEvent(event);
        } else if (event instanceof BreakpointEvent) {
            return breakpointEvent(event);
        } else if (event instanceof WatchpointEvent) {
            return fieldWatchEvent(event);
        } else if (event instanceof StepEvent) {
            return stepEvent(event);
        } else if (event instanceof MethodEntryEvent) {
            return methodEntryEvent(event);
        } else if (event instanceof MethodExitEvent) {
            return methodExitEvent(event);
        } else if (event instanceof ClassPrepareEvent) {
            ClassPrepareEvent cpe = (ClassPrepareEvent)event;
            type = cpe.referenceType();
            try{
                List methods = type.methods();
                for(int i = 0; i < methods.size(); i++)
                {
                    Method temp = (Method)methods.get(i);
                    Location line = temp.location();
                    BreakpointRequest breakpoint = vm.eventRequestManager()
                            .createBreakpointRequest(line);
                    breakpoint.enable();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return classPrepareEvent(event);
        } else if (event instanceof ClassUnloadEvent) {
            return classUnloadEvent(event);
        } else if (event instanceof ThreadStartEvent) {
            return threadStartEvent(event);
        } else if (event instanceof ThreadDeathEvent) {
            return threadDeathEvent(event);
        } else if (event instanceof VMStartEvent) {
            return vmStartEvent(event);
        } else {
            return handleExitEvent(event);
        }
    }

    private boolean vmDied = false;

    private boolean handleExitEvent(Event event) {
        if (event instanceof VMDeathEvent) {
            vmDied = true;
            return vmDeathEvent(event);
        } else if (event instanceof VMDisconnectEvent) {
            connected = false;
            if (!vmDied) {
                vmDisconnectEvent(event);
            }
            return false;
        } else {
            throw new InternalError("Unexpected event type");
        }
    }

    private boolean vmStartEvent(Event event) {
        VMStartEvent se = (VMStartEvent) event;
        return stopOnVMStart;
    }

    private boolean breakpointEvent(Event event) {
        //
        BreakpointEvent breakpointEvent = (BreakpointEvent)event;
        if(hashTable.containsKey(breakpointEvent.location().method().toString()))
        {
            int temp = (Integer)hashTable.get(breakpointEvent.location().method().toString());
            hashTable.put(breakpointEvent.location().method().toString(), (temp + 1));
            
        }
        else
        {
            hashTable.put(breakpointEvent.location().method().toString(), 1);
        }
        ct.updateNumbers();
        return false;
    }

    private boolean methodEntryEvent(Event event) {
        MethodEntryEvent me = (MethodEntryEvent) event;
        System.out.println("MethodEntryEvent");
        System.out.println(me.method().toString());
        System.out.println(me.location().lineNumber());
        return true;
    }

    private boolean methodExitEvent(Event event) {
        MethodExitEvent me = (MethodExitEvent) event;
        return true;
    }

    private boolean fieldWatchEvent(Event event) {
        WatchpointEvent fwe = (WatchpointEvent) event;
        return true;
    }

    private boolean stepEvent(Event event) {
        StepEvent se = (StepEvent) event;
        return true;
    }

    private boolean classPrepareEvent(Event event) {
        ClassPrepareEvent cle = (ClassPrepareEvent) event;
        return false;
    }

    private boolean classUnloadEvent(Event event) {
        ClassUnloadEvent cue = (ClassUnloadEvent) event;
        return false;
    }

    private boolean exceptionEvent(Event event) {
        ExceptionEvent ee = (ExceptionEvent) event;
        return true;
    }

    private boolean threadDeathEvent(Event event) {
        ThreadDeathEvent tee = (ThreadDeathEvent) event;
        return false;
    }

    private boolean threadStartEvent(Event event) {
        ThreadStartEvent tse = (ThreadStartEvent) event;
        return false;
    }

    public boolean vmDeathEvent(Event event) {
        return false;
    }

    public boolean vmDisconnectEvent(Event event) {
        System.out.println("VMDisconnectEvent");
        return false;
    }

    private void jbInit() throws Exception {
    }

}