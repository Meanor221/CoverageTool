/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coveragetool;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Hashtable;

/**
 *
 * @author aam5617
 */
public class ClassOutline {
    Constructor[] ca;
    Method[] ma;
    
    /***
     * Puts the constructors and methods into the lists from the needed object
     * @param c the class that it is getting methods and constructors from
     * @param ct the GUI object that is running it so that the text areas can be updated
     * @throws ClassNotFoundException
     * @throws MalformedURLException 
     */
    public void classSetup(Class c, CoverageTool ct) throws ClassNotFoundException, MalformedURLException
    {
        ma = c.getDeclaredMethods();
        ca = c.getConstructors();
        String complete = "";
        String numbersArea = "";
        if(ca.length > 0)
        {
            String[] constructorNames = new String[ca.length];
            Class[] parameterTypes;
            for(int i = 0; i < ca.length; i++)
            {
                String parameters = "";
                parameterTypes = ca[i].getParameterTypes();
                if(parameterTypes.length > 0)
                {
                    for(int j = 0; j < parameterTypes.length; j++)
                    {
                        if(j == parameterTypes.length - 1)
                        {
                            parameters = parameters.concat(parameterTypes[j].getSimpleName());
                        }
                        else
                        {
                            parameters = parameters.concat(parameterTypes[j].getSimpleName() + ", ");
                        }
                    }
                }
                constructorNames[i] = ca[i].getName() + "(" + parameters + ")";
            }
            for(int i = 0; i < constructorNames.length; i++)
            {
                complete = complete.concat(constructorNames[i]) + "\n";
                numbersArea = numbersArea.concat("0\n");
            }
        }
        if(ma.length > 0)
        {
            String[] methodNames = new String[ma.length];
            Class[] parameterTypes;
            for(int i = 0; i < ma.length; i++)
            {
                String parameters = "";
                parameterTypes = ma[i].getParameterTypes();
                if(parameterTypes.length > 0)
                {
                    for(int j = 0; j < parameterTypes.length; j++)
                    {
                        if(j == parameterTypes.length - 1)
                        {
                            parameters = parameters.concat(parameterTypes[j].getSimpleName());
                        }
                        else
                        {
                            parameters = parameters.concat(parameterTypes[j].getSimpleName() + ", ");
                        }
                    }
                }
                methodNames[i] = ma[i].getName() + "(" + parameters + ")";
            }
            for(int i = 0; i < methodNames.length; i++)
            {
                complete = complete.concat(methodNames[i]) + "\n";
                numbersArea = numbersArea.concat("0\n");
            }
        }
        ct.getMethodArea().setText(complete);
        ct.getNumbers().setText(numbersArea);
    }
    
    /**
     * Does basically the same thing as the above class however it is used after a class has been ran
     * @param c the class that it is getting methods and constructors from
     * @param ct the GUI object that is running it so that the text areas can be updated
     * @param currentValues the hashtable that holds the numbers
     */
    public void update(Class c, CoverageTool ct, Hashtable currentValues)
    {
        ma = c.getDeclaredMethods();
        ca = c.getDeclaredConstructors();
        String complete = "";
        String numbersArea = "";
        if(ca.length > 0)
        {
            // Used for the search-ready names of the constructors
            String[] constructorNames = new String[ca.length];
            // Used to hold the formatted names of the constructors
            String[] formattedConstructorNames = new String[ca.length];
            Class[] parameterTypes;
            for(int i = 0; i < ca.length; i++)
            {
                String parameters = "";
                parameterTypes = ca[i].getParameterTypes();
                if(parameterTypes.length > 0)
                {
                    for(int j = 0; j < parameterTypes.length; j++)
                    {
                        if(j == parameterTypes.length - 1)
                        {
                            parameters = parameters.concat(parameterTypes[j].getSimpleName());
                        }
                        else
                        {
                            parameters = parameters.concat(parameterTypes[j].getSimpleName() + ", ");
                        }
                    }
                }
                //Formatting the string so that it will search for the correct string
                int classNameBeginIndex = ca[i].toString().lastIndexOf(" ");
                int firstParend = ca[i].toString().lastIndexOf("(");
                constructorNames[i] = ca[i].toString().substring(classNameBeginIndex + 1);
                constructorNames[i] = constructorNames[i].substring(0, constructorNames[i].length() - 2);
                formattedConstructorNames[i] = ca[i].getName() + "(" + parameters + ")";
            }
            for(int i = 0; i < constructorNames.length; i++)
            {
                complete = complete.concat(formattedConstructorNames[i]) + "\n";
                String search = constructorNames[i];
                search = search.substring(search.lastIndexOf(" ") + 1);
                search = search.replaceFirst(constructorNames[i], constructorNames[i] + ".<init>()");
                Integer numRuns = (Integer)currentValues.get(search);
                if(numRuns != null)
                {
                    numbersArea = numbersArea.concat(numRuns + "\n");
                }
                else
                    numbersArea = numbersArea.concat("0\n");
            }
        }
        if(ma.length > 0)
        {
            // Used for the search-ready names of the methods
            String[] methodNames = new String[ma.length];
            // Used to hold the formatted names of the methods
            String[] formattedMethodNames = new String[ma.length];
            Class[] parameterTypes;
            for(int i = 0; i < ma.length; i++)
            {
                String parameters = "";
                parameterTypes = ma[i].getParameterTypes();
                if(parameterTypes.length > 0)
                {
                    for(int j = 0; j < parameterTypes.length; j++)
                    {
                        if(j == parameterTypes.length - 1)
                        {
                            parameters = parameters.concat(parameterTypes[j].getSimpleName());
                        }
                        else
                        {
                            parameters = parameters.concat(parameterTypes[j].getSimpleName() + ", ");
                        }
                    }
                }
                //Formatting the string so that it will search for the correct string
                int classNameBeginIndex = ma[i].toString().lastIndexOf(" ");
                methodNames[i] = ma[i].toString().substring(classNameBeginIndex + 1);
                formattedMethodNames[i] = ma[i].getName() + "(" + parameters + ")";
            }
            for(int i = 0; i < methodNames.length; i++)
            {
                complete = complete.concat(formattedMethodNames[i]) + "\n";
                String search = methodNames[i];
                Integer numRuns = (Integer)currentValues.get(search);
                if(numRuns != null)
                {
                    numbersArea = numbersArea.concat(numRuns + "\n");
                }
                else
                    numbersArea = numbersArea.concat("0\n");
            }
        }
        ct.getMethodArea().setText(complete);
        ct.getNumbers().setText(numbersArea);
    }
}
