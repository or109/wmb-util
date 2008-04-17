package com.googlecode.nodes.qsys;

import com.ibm.as400.access.AS400;

public class Call {
    
    /**
     * arg[0] = host
     * arg[1] = user name
     * arg[2] = password
     * arg[3] = command
     */
    public static void main(String[] args) throws Exception {
        AS400 sys = new AS400(args [0], args [1], args [2]);
        
        new Command(args[3]).call(sys);
        
        sys.disconnectAllServices();
        
    }}
