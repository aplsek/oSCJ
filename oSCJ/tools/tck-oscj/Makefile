_all:
	$(MAKE) all

checkdeps:
	echo "# generated file -- do not edit" > depend.mk
	echo "SRCFILES=\\" >> depend.mk
	echo `find src/ | grep \\\.java$$ | grep -v /ovm/` >> depend.mk

Makefile:
	echo "doing nothing"

%: checkdeps
	echo $@
	rm -f .phony
	./config.status
	$(MAKE) -f RealMakefile $@

clean-scj-tests:
	rm -rf build
	rm -rf enterPrivate*
	rm -rf hello*
	rm -rf immortalTest*
	mkdir build
	
