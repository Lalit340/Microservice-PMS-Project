package in.co.indusnet.rekyc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.co.indusnet.rekyc.model.File;


@Repository
public interface FileRepository extends JpaRepository<File, Integer>{

}
