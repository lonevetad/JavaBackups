stats=["Strength", "Constitution", "Health", "Defense", "Dexterity", "Precision", "Intelligence", "Wisdom", "Faith"]
a=[]
minusc="abcdefghijklmnopqrstuvwxyz"
maiusc="ABCDEFGHIJKLMNOPQRSTUVWXYZ"

r= (max) =>{ return Math.floor(Math.random() * max); }
stats.forEach((v1, i1,_)=>{
	stats.forEach((v2, i2,_)=>{
	if(i1 != i2){
	    let len = r(6)+4;
	    let n="";
	    while(len > 0){
	      n = n + minusc.charAt(r(26));
	    }
	    a.push({
	      "name": (maiusc.charAt(r(26))+n),
	      "rarity": 1,
	      "price":[8],
	      "attributeModifiers": {
	         v1: 7,
	         v2: -2
	      }
	    })
	}
})
})

const j = JSON.stringify(a, null, 4)

console.log(j)