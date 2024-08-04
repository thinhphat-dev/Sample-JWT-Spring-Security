import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import {jwtDecode} from 'jwt-decode'; // Correct the import here
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { confirmAlert } from 'react-confirm-alert'; // Import
import 'react-confirm-alert/src/react-confirm-alert.css'; // Import css

const Teacher = () => {
  const token = localStorage.getItem('auth_token');

  const [teachers, setTeachers] = useState([]);
  const [formData, setFormData] = useState({
    id: 0,
    maGiangVien: '',
    emailCaNhan: '',
    emailEdu: '',
    hinhDaiDien: '',
    hoGiangVien: '',
    ngaySinh: new Date(),
    soDienThoai: '',
    tenGiangVien: ''
  });

  const [errors, setErrors] = useState({});
  const [showModal, setShowModal] = useState(false);
  const [modalTitle, setModalTitle] = useState('Thêm giảng viên');

  const getAuthHeader = useCallback(() => {
    return { Authorization: `Bearer ${token}` };
  }, [token]);

  const getTeachers = useCallback(async () => {
    try {
      const res = await axios.get('http://localhost:8081/api/teachers', { headers: getAuthHeader() });
      // console.log('Response data:', res.data); // Kiểm tra phản hồi từ API
      if (res.data && Array.isArray(res.data)) {
        setTeachers(res.data);
      } else {
        setTeachers([]);
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  }, [getAuthHeader]);

  useEffect(() => {
    getTeachers();
  }, [getTeachers]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleFocus = (e) => {
    const { name } = e.target;
    setErrors({ ...errors, [name]: '' });
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setFormData({ ...formData, hinhDaiDien: URL.createObjectURL(file) });
      setErrors({ ...errors, hinhDaiDien: '' });
    }
  };

  const handleDateChange = (date) => {
    setFormData({ ...formData, ngaySinh: date });
    setErrors({ ...errors, ngaySinh: '' });
  };

  const getUserRole = () => {
    if (token) {
      const decodedToken = jwtDecode(token);
      const sub = decodedToken.sub;
      if (sub) {
        const role = extractRole(sub);
        return role;
      }
    }
    return null;
  };

  const extractRole = (sub) => {
    const rolePattern = /role=([^,]*)/;
    const match = sub.match(rolePattern);
    if (match) {
      return match[1].trim();
    }
    return null;
  };

  const validateForm = () => {
    const newErrors = {};
    const emailCaNhanRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const emailEduRegex = /^[^\s@]+@fpt\.edu\.vn$/;
    const phoneRegex = /^0\d{9}$/;

    if (!formData.maGiangVien) newErrors.maGiangVien = 'Mã giảng viên không được để trống';
    if (!formData.emailCaNhan) newErrors.emailCaNhan = 'Email cá nhân không được để trống';
    else if (!emailCaNhanRegex.test(formData.emailCaNhan)) newErrors.emailCaNhan = 'Email cá nhân không hợp lệ';
    if (!formData.emailEdu) newErrors.emailEdu = 'Email edu không được để trống';
    else if (!emailEduRegex.test(formData.emailEdu)) newErrors.emailEdu = 'Email edu phải có dạng @fpt.edu.vn';
    if (!formData.hoGiangVien) newErrors.hoGiangVien = 'Họ giảng viên không được để trống';
    if (!formData.tenGiangVien) newErrors.tenGiangVien = 'Tên giảng viên không được để trống';
    if (!formData.soDienThoai) newErrors.soDienThoai = 'Số điện thoại không được để trống';
    else if (!phoneRegex.test(formData.soDienThoai)) newErrors.soDienThoai = 'Số điện thoại phải là 10 số và bắt đầu bằng số 0';
    if (!formData.hinhDaiDien) newErrors.hinhDaiDien = 'Hình đại diện không được để trống';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleAddTeacher = async (e) => {
    e.preventDefault();

    const userRole = getUserRole();
    if (userRole !== 'admin') {
      toast.error('Bạn không có quyền thực hiện thao tác này.');
      return;
    }

    if (!validateForm()) {
      toast.error('Vui lòng điền đầy đủ các trường bắt buộc.');
      return;
    }

    const formattedFormData = {
      ...formData,
      ngaySinh: formData.ngaySinh.toISOString().split('T')[0]  // Chuyển đổi sang định dạng YYYY-MM-DD
    };

    try {
      if (formData.id === 0) {
        const res = await axios.post('http://localhost:8081/api/teachers', formattedFormData, { headers: getAuthHeader() });
        setTeachers([...teachers, res.data]);
        toast.success('Thêm giảng viên thành công!');
      } else {
        await axios.put(`http://localhost:8081/api/teachers/${formData.id}`, formattedFormData, { headers: getAuthHeader() });
        const updatedTeachers = teachers.map(teacher =>
          teacher.id === formData.id ? formData : teacher
        );
        setTeachers(updatedTeachers);
        toast.success('Cập nhật giảng viên thành công!');
      }
      clearForm();
      getTeachers();
      setShowModal(false);
    } catch (error) {
      console.error('Error adding/editing data:', error);
      if (error.response) {
        console.error('Data:', error.response.data);
        console.error('Status:', error.response.status);
        console.error('Headers:', error.response.headers);
        toast.error(`Error: ${error.response.data.message}`);
      } else if (error.request) {
        console.error('Request:', error.request);
        toast.error('Không thể kết nối tới server.');
      } else {
        console.error('Error', error.message);
        toast.error(`Lỗi: ${error.message}`);
      }
    }
  };

  const handleDeleteTeacher = async (id) => {
    const userRole = getUserRole();
    if (userRole !== 'admin') {
      toast.error('Bạn không có quyền thực hiện thao tác này.');
      return;
    }

    confirmAlert({
      title: 'Xác nhận xóa',
      message: 'Bạn có chắc chắn muốn xóa thông tin này không?',
      buttons: [
        {
          label: 'Có',
          onClick: async () => {
            try {
              await axios.delete(`http://localhost:8081/api/teachers/${id}`, { headers: getAuthHeader() });
              setTeachers(teachers.filter(teacher => teacher.id !== id));
              toast.success('Xóa giảng viên thành công!');
            } catch (error) {
              console.error('Error deleting data:', error);
              toast.error('Xóa giảng viên thất bại.');
            }
          }
        },
        {
          label: 'Không',
          onClick: () => { }
        }
      ]
    });
  };

  const handleEditTeacher = (teacher) => {
    setFormData({
      id: teacher.id,
      maGiangVien: teacher.maGiangVien,
      emailCaNhan: teacher.emailCaNhan,
      emailEdu: teacher.emailEdu,
      hinhDaiDien: teacher.hinhDaiDien,
      hoGiangVien: teacher.hoGiangVien,
      ngaySinh: new Date(teacher.ngaySinh),
      soDienThoai: teacher.soDienThoai,
      tenGiangVien: teacher.tenGiangVien
    });
    setModalTitle('Sửa giảng viên');
    setShowModal(true);
  };

  const clearForm = () => {
    setFormData({
      id: 0,
      maGiangVien: '',
      emailCaNhan: '',
      emailEdu: '',
      hinhDaiDien: '',
      hoGiangVien: '',
      soDienThoai: '',
      tenGiangVien: '',
      ngaySinh: new Date()
    });
    setErrors({});
    setModalTitle('Thêm giảng viên');
  };

  return (
    <div className="container-fluid mx-auto mt-5">
      <ToastContainer />
      <div className="row">
        <div className="col-md-12">
          <h3 className="mb-4">Danh sách giảng viên</h3>
          <table className="table table-striped table-bordered">
            <thead className="thead-dark">
              <tr>
                <th>Thứ tự</th>
                <th>Mã Giảng viên</th>
                <th>Email Cá nhân</th>
                <th>Email Edu</th>
                <th>Hình đại diện</th>
                <th>Họ Giảng viên</th>
                <th>Số Điện thoại</th>
                <th>Tên Giảng viên</th>
                <th>Ngày Sinh</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {teachers && teachers.length > 0 ? (
                teachers.map((teacher, index) => (
                  teacher && teacher.id ? (
                    <tr key={teacher.id}>
                      <td>{index + 1}</td>
                      <td>{teacher.maGiangVien}</td>
                      <td>{teacher.emailCaNhan}</td>
                      <td>{teacher.emailEdu}</td>
                      <td>
                        <img src={teacher.hinhDaiDien} alt="Hình đại diện" style={{ maxWidth: '100px', maxHeight: '100px' }} />
                      </td>
                      <td>{teacher.hoGiangVien}</td>
                      <td>{teacher.tenGiangVien}</td>
                      <td>{teacher.soDienThoai}</td>
                      <td>{new Date(teacher.ngaySinh).toLocaleDateString('en-GB')}</td>
                      <td className="d-flex">
                        <button
                          onClick={() => handleDeleteTeacher(teacher.id)}
                          className="btn btn-danger mr-2">
                          Xóa
                        </button>
                        <button
                          onClick={() => handleEditTeacher(teacher)}
                          className="btn btn-primary">
                          Sửa
                        </button>
                      </td>
                    </tr>
                  ) : (
                    <tr key={`invalid-${index}`}>
                      <td colSpan="10">Dữ liệu không hợp lệ</td>
                    </tr>
                  )
                ))
              ) : (
                <tr>
                  <td colSpan="10">Không có giảng viên nào.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      <div className="col-md-3">
        <button
          className="btn btn-success mt-4"
          onClick={() => {
            setShowModal(true);
            setModalTitle('Thêm giảng viên');
            clearForm();
          }}
        >
          Thêm giảng viên
        </button>
      </div>

      <div className={`modal ${showModal ? 'show' : ''}`} tabIndex="-1" role="dialog" style={{ display: showModal ? 'block' : 'none' }}>
        <div className="modal-dialog" role="document">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title">{modalTitle}</h5>
              <button type="button" className="close " data-dismiss="modal" aria-label="Close" onClick={() => setShowModal(false)}>
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div className="modal-body">
              <form onSubmit={handleAddTeacher}>
                <div className="form-group">
                  <label>Mã Giảng viên</label>
                  <input
                    type="text"
                    name="maGiangVien"
                    value={formData.maGiangVien}
                    onChange={handleInputChange}
                    onFocus={handleFocus}
                    className="form-control"
                  />
                  {errors.maGiangVien && <small className="text-danger">{errors.maGiangVien}</small>}
                </div>
                <div className="form-group">
                  <label>Email Cá nhân</label>
                  <input
                    type="text"
                    name="emailCaNhan"
                    value={formData.emailCaNhan}
                    onChange={handleInputChange}
                    onFocus={handleFocus}
                    className="form-control"
                  />
                  {errors.emailCaNhan && <small className="text-danger">{errors.emailCaNhan}</small>}
                </div>
                <div className="form-group">
                  <label>Email Edu</label>
                  <input
                    type="text"
                    name="emailEdu"
                    value={formData.emailEdu}
                    onChange={handleInputChange}
                    onFocus={handleFocus}
                    className="form-control"
                  />
                  {errors.emailEdu && <small className="text-danger">{errors.emailEdu}</small>}
                </div>
                <div className="form-group">
                  <label>Hình đại diện</label>
                  <input
                    type="file"
                    accept="image/*"
                    onChange={handleImageChange}
                    className="form-control"
                  />
                  {errors.hinhDaiDien && <small className="text-danger">{errors.hinhDaiDien}</small>}
                  {formData.hinhDaiDien && (
                    <img src={formData.hinhDaiDien} alt="Preview" style={{ maxWidth: '100px', maxHeight: '100px', marginTop: '10px' }} />
                  )}
                </div>
                <div className="form-group">
                  <label>Họ Giảng viên</label>
                  <input
                    type="text"
                    name="hoGiangVien"
                    value={formData.hoGiangVien}
                    onChange={handleInputChange}
                    onFocus={handleFocus}
                    className="form-control"
                  />
                  {errors.hoGiangVien && <small className="text-danger">{errors.hoGiangVien}</small>}
                </div>
                <div className="form-group">
                  <label>Tên Giảng viên</label>
                  <input
                    type="text"
                    name="tenGiangVien"
                    value={formData.tenGiangVien}
                    onChange={handleInputChange}
                    onFocus={handleFocus}
                    className="form-control"
                  />
                  {errors.tenGiangVien && <small className="text-danger">{errors.tenGiangVien}</small>}
                </div>
                <div className="form-group">
                  <label>Số Điện thoại</label>
                  <input
                    type="text"
                    name="soDienThoai"
                    value={formData.soDienThoai}
                    onChange={handleInputChange}
                    onFocus={handleFocus}
                    className="form-control"
                  />
                  {errors.soDienThoai && <small className="text-danger">{errors.soDienThoai}</small>}
                </div>
                <div className="form-group">
                  <label>Ngày Sinh</label>
                  <br />
                  <DatePicker
                    selected={formData.ngaySinh}
                    onChange={handleDateChange}
                    onFocus={handleFocus}
                    dateFormat="dd/MM/yyyy"
                    className="form-control"
                  />
                </div>
                <button type="submit" className="btn btn-success">
                  Lưu
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Teacher;
