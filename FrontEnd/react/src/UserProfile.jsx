
const UserProfile = ({name, age, gender,imageNumber}) => {
    gender = gender === "MALE"?"men":"women";
    return(  
    <div>
        <h1>{name}</h1>
        <p>{age}</p>
        <img 
        src={`https://randomuser.me/api/portraits/med/${gender}/${imageNumber}.jpg`} alt="" />
     
    </div>
    )
}
export default UserProfile;