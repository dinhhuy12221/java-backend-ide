const
now =
    new Date
();
const day =
    
    
    now.getDay()
    
    ; // returns a number representing the day of the week, starting with 0 for Sunday
const hours =
    now.getHours();
const minutes = now.getMinutes();
console.log(`Today is day $ {day} and the time is $ {hours}: $ {minutes}.`
            
           );