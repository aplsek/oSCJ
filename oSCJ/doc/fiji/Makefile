#  
# INSTRUCTIONS:
#  1) Check out Fiji: 
#     hg clone ssh://zed.cs.purdue.edu//p/sss/fivmhg/fivm
#     NOTE: The fiji hash should be "5eac3e94a8a9" !!!!   
#
#  2) Patch Fiji: Patches for specific hashes are stored in $SCJ_HOME/examples/fiji/fiji-patch/$HASH.
#     Simply patch them in order. For example, for Fiji 5eac3e94a8a9:
#
#       $ patch -p1 < $SCJ_HOME/examples/fiji/fiji-patch/5eac3e94a8a9/1_fivmc_scj.diff
#       $ patch -p1 < $SCJ_HOME/examples/fiji/fiji-patch/5eac3e94a8a9/2_scj_minimal.diff
#       $ patch -p1 < $SCJ_HOME/examples/fiji/fiji-patch/5eac3e94a8a9/3_scj_wrapper.diff  
#       $ autoreconf -i
#       $ ./configure
#       $ make
#  
#  3) updating the old Patch (from http://drupal.org/patch/reverse)
#     You should also reverse a patch prior to adding a newer, updated version of the same patch. 
#     To reverse the patch, use the patch command with the -R option:
#       $ patch -p1 -R < path/file.patch 
#         
#  4) revision info
#     hg identify
#
#  5) Helloworld directory
#     scj/
#         bin/
#         ri/
#         examples/
#             helloworld/
#                 src/
#                 bin/
#                 build/
#
#  Incidentally, the argument passed to --scj-safelet is a class name,
#  not a filename; use MyHelloWorld (no .class extension).  You'll still
#  have to provide the MyHelloWorld.class file on the command line,
#  however:
#
#  fivmc -o hello -v 1 --scj "MyHelloWorld.class" "MyHelloWorld\$1.class" \
#    "MyHelloWorld\$WordHandler.class" --scj-safelet MyHelloWorld 
#
#  6) To compile and run HelloWorld, simply run make. You can change the path to Fiji by doing
#
#     $ make FIJI=/path/to/fiji
#
#

CDX_HOME=/Users/leizhao/Documents/workspace/java/miniCDx-J4
HELLO_HOME=/Users/leizhao/Documents/workspace/java/scj/examples/helloworld
SCJ_HOME=/Users/leizhao/Documents/workspace/java/scj
FIJI_HOME=/Users/leizhao/Documents/workspace/java/fivm


############################    All    ##############################
all:
	echo "No all! Choose one: hello | cdx"

############################    Compile    ##############################
hello: sjar
	rm -rf hello.build
	mkdir hello.build
	javac -cp $(FIJI_HOME)/lib/scj.jar -d hello.build $(HELLO_HOME)/src/*.java
	$(FIJI_HOME)/bin/fivmc -o myhello -v 1 --scj hello.build/*.class --scj-safelet MyHelloWorld 

cdx: sjar
	rm -rf cdx.build
	mkdir cdx.build
	find $(CDX_HOME)/cdx -name *.java > list
	find $(CDX_HOME)/simulator -name *.java >> list
	find $(CDX_HOME)/utils -name *.java >> list
	javac -cp $(FIJI_HOME)/lib/scj.jar -d cdx.build @list
	find cdx.build -name *.class > list
	sed s/\\$$/\\\\$$/g list > list
	$(FIJI_HOME)/bin/fivmc -o mycdx -v 1 --scj $(shell cat class_list) --scj-safelet cdx.Level0Safelet

############################    SCJ library    ##############################
sjar:
	rm -rf scj.build
	mkdir scj.build
	find $(SCJ_HOME)/ri/spec/common/src -name *.java > list
	find $(SCJ_HOME)/ri/s3/common/src -name *.java >> list
	find $(SCJ_HOME)/ri/spec/fiji/src -name *.java >> list
	find $(SCJ_HOME)/ri/s3/fiji/src -name *.java >> list
	javac -d scj.build @list
	jar cf scj.jar -C scj.build edu -C scj.build javax
	cp scj.jar $(FIJI_HOME)/lib/

############################    Run    ##############################
runHello:  
	./$(NAME) MyHelloWorld

############################    Clean    ##############################
cleanAll: 
	


