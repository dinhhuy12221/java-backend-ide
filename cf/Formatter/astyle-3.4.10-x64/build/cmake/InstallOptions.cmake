# Define install directories
# To uninstall 'xargs rm < install_manifest.txt'
# Default linux install prefix is /usr/local"
# This may be modified by -DCMAKE_INSTALL_PREFIX=
# Default Win32 install prefix is not used (C:/Program Files (x86))
if(BUILD_SHARED_LIBS OR BUILD_STATIC_LIBS)
    if(NOT WIN32)
        install(TARGETS astyle DESTINATION lib)
    endif()
else()
    if(WIN32)
        set(pf86 "PROGRAMFILES(x86)")
        set(prog_files $ENV{${pf86}})
        if(NOT ${prog_files})
            set(prog_files $ENV{PROGRAMFILES})
        endif()
        install(TARGETS astyle DESTINATION "${prog_files}/AStyle")
        install(FILES ${DOCS} DESTINATION "${prog_files}/AStyle/doc")
    elseif(APPLE)
        # install to the default /usr/local/bin because of SIP restrictions
        install(TARGETS astyle DESTINATION "${CMAKE_INSTALL_PREFIX}/bin")
        install(FILES ${DOCS} DESTINATION "${CMAKE_INSTALL_PREFIX}/share/doc/astyle/html")
    else()
        # change default to /usr/bin, the same as package installs
        if(CMAKE_INSTALL_PREFIX_INITIALIZED_TO_DEFAULT)
            set(CMAKE_INSTALL_PREFIX "/usr")
		endif()
        install(TARGETS astyle DESTINATION "${CMAKE_INSTALL_PREFIX}/bin")
        install(FILES ${DOCS} DESTINATION "${CMAKE_INSTALL_PREFIX}/share/doc/astyle/html")
        INSTALL(FILES ${MAN} DESTINATION "${CMAKE_INSTALL_PREFIX}/man/man1")
	endif()
endif()
