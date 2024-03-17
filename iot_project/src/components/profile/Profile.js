
import avt from '../../images/avt.jpg'
import './styleProfile.css'
import { openDashBoard } from '../../funtion/funtion';

function Profile() {

    const handleClickBar = () => {
        openDashBoard(true)
    }
    return (
        <div className="profile-container">
            <div className='bar' onClick={handleClickBar}>
                <i class="fa-solid fa-bars"></i>
            </div>
            <div className='img-inf-user'>
                <img src={avt} alt='avt'></img>
                <h3 className='my-name'>Nguyễn Thành Phước</h3>
            </div>
            <div className='element-inf-user'>
                <div className='information inf1'>
                    <h3>Thông tin</h3>
                    <p>Họ và tên: Nguyễn Thành Phước</p>
                    <p>Mã sinh viên: B20DCCN516</p>
                    <p>Trường: PTIT</p>
                </div>
                <div className='contact inf1'>
                    <h3>Liên hệ</h3>
                    <p>Email: thanhphuocx10@gmail.com</p>
                    <p>Sđt: 0868662785</p>
                </div>

            </div>

        </div>
    );
}

export default Profile;