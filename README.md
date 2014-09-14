oSCJ
===============
Open Safety Critical Java (oSCJ) project implements a restricted subset of SCJ (JSR-302). The project is developed at S3 group at Purdue University.

For more information, see the oSCJ webpage: http://www.ovmj.net/oscj/


Obtaining oSCJ's VM
==============================
oSCJ library can be executed on top of two different Virtual Machines : FijiVM and Ovm.

FijiVM - FijiVM is available under academic license, feel free to directly contact us to obtain its distribution. FijiVM' usage is surprisingly simple and while delivering astonishing performance! (See FijiVM Installation instructions: http://rtjava.blogspot.com/2010/10/oscj-news.html)
To install FijiVM+oSCJ:
1. untar fivm.tar.gz
2. cd fivm
3. $ hg clone https://scj-jsr302.googlecode.com/hg/ scj
4. execute:
  $ autoreconf -i
  $ ./configure
  $ make

To update oSCJ:
cd fivm/scj
$ hg pull
$ hg update

All you need is located in fivm/scj/oSCJ.
- there is examples/ dir with all the example applications and also "scj/" dir
    that contains oSCJ L0 implementation. 
 Just enter e.g. fivm/scj/oSCJ/examples/helloworld and type ./build.sh
- you can also observe all the building parameters 
- for a more complicated case study and build parameters, see fivm/scj/oSCJ/examples/minicdx 

Ovm 
============================
- You can check-out our Ovm distribution from :
hg clone https://ovm.scj-jsr302.googlecode.com/hg/ ovm  
See our OVM readme file for more details about installing and running oSCJ with Ovm.


Origina oSCJ repository available at https://code.google.com/p/scj-jsr302/
