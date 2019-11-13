import React, {Component} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './Moinguoi.css'

class Moinguoi extends Component {
    render() {
        return (
            <div className="container">
                <div>
                    <div className="giaovien mt-5">
                        <h2>Giáo viên</h2>
                        <img src="https://img.icons8.com/dotty/30/000000/add-administrator.png"/>
                    </div>
                    <div className="data">
                        <div>aaaa</div>
                        <div>aaaa</div>
                        <div>aaaa</div>
                        <div>aaaa</div>
                        <div>aaaa</div>
                        <div>aaaa</div>
                    </div>
                </div>
                <div className="giaovien mt-5">
                    <h2>Sinh viên</h2>
                    <img src="https://img.icons8.com/dotty/30/000000/add-administrator.png"/>
                </div>
                <div className="data">
                    <div>aaaa</div>
                    <div>aaaa</div>
                    <div>aaaa</div>
                    <div>aaaa</div>
                    <div>aaaa</div>
                    <div>aaaa</div>
                </div>
            </div>
        );
    }
}

export default Moinguoi;