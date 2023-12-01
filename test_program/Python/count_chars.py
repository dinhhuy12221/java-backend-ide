
# Python code to find no. of common characters between two strings
 
#first covert both string to set and then find common character between two
def                 commonfun(str1,str2):
    return(len
           ((set
                    
(str1))         .intersection
        (set(str2))))

#string1=input("Enter String 1 : ")
string1     =input(
    "Enter string 1: ")
string2=        input("Enter string 2: "
                )
#string2=input("Enter String 2 : ")
 
no_of_common_character=commonfun        (string1
                                         .lower(),
                                         string2
                                         .lower())
 
print
(  "NO. OF COMMON CHRACTERS ARE : "
        ,no_of_common_character)
     
#this code is contributed by VISHAV PRATAP SINGH RANA(UIET KURUKSHETRA)
